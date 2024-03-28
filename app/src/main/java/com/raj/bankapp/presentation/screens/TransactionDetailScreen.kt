package com.raj.bankapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.raj.bankapp.R
import com.raj.bankapp.presentation.base.CommonUI
import com.raj.bankapp.presentation.base.CommonUI.getIconResourceForCategory
import com.raj.bankapp.presentation.viewmodel.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    navController: NavController,
    transactionId: String,
    accountViewModel: AccountViewModel = hiltViewModel()
) {
    val transaction = accountViewModel.getTransactionById(transactionId)
    Scaffold(
        topBar = {
            TopAppBar(title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back_arrow)
                        )
                    }
                }

            )
        }
    ) { scaffoldPadding ->
        Box(modifier = Modifier.padding(scaffoldPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                transaction?.let { data ->
                    Icon(
                        painter = painterResource(id = getIconResourceForCategory(data.category)),
                        contentDescription = data.category,
                        modifier = Modifier.size(64.dp),
                    )
                    Text(text = transaction.category, style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = CommonUI.formatAmount(data.amount),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = data.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}