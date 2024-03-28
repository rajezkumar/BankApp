package com.raj.bankapp.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.raj.bankapp.presentation.base.UIState
import com.raj.bankapp.domain.model.MainAccountListItem
import com.raj.bankapp.presentation.screens.AccountInfoScreen
import com.raj.bankapp.presentation.screens.TransactionDetailScreen
import com.raj.bankapp.presentation.viewmodel.AccountViewModel
import com.raj.bankapp.presentation.theme.BankTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class Screen(val route: String) {
    data object AccountInfo : Screen("AccountInfo")
    data object TransactionDetail : Screen("TransactionDetails/{transactionId}") {
        fun createRoute(transactionId: String) = "TransactionDetails/$transactionId"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BankTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Account(modifier = Modifier)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Account(
        modifier: Modifier = Modifier,
        accountViewModel: AccountViewModel = hiltViewModel()
    ) {
        LaunchedEffect(key1 = true) {
            accountViewModel.getAccountData()
        }
        val state by accountViewModel.accountInfo.collectAsState()
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Screen.AccountInfo.route
        ) {
            composable(Screen.AccountInfo.route) {
                when (state) {
                    is UIState.Success -> {
                        val data = (state as UIState.Success<List<MainAccountListItem>>).data
                        AccountInfoScreen(
                            data,
                            modifier,
                            navController
                        )
                    }

                    is UIState.Error -> {
                        val error = (state as UIState.Error).errorMessage
                        Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
                    }

                    is UIState.Empty -> {
                        //do nothing
                    }
                }
            }
            composable(
                Screen.TransactionDetail.route,
                arguments = listOf(navArgument("transactionId") {
                    type = NavType.StringType
                })
            ) {
                val transactionId = it.arguments?.getString("transactionId") ?: ""
                TransactionDetailScreen(navController, transactionId, accountViewModel)
            }
        }
    }
}


