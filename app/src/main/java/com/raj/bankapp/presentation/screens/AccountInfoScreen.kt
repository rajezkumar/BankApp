package com.raj.bankapp.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.raj.bankapp.R
import com.raj.bankapp.presentation.Screen
import com.raj.bankapp.domain.model.AccountBalance
import com.raj.bankapp.domain.model.AccountDetails
import com.raj.bankapp.domain.model.AccountName
import com.raj.bankapp.domain.model.ItemType
import com.raj.bankapp.domain.model.MainAccountListItem
import com.raj.bankapp.domain.model.SectionDivider
import com.raj.bankapp.domain.model.Transaction
import com.raj.bankapp.presentation.base.CommonUI.formatAmount
import com.raj.bankapp.presentation.base.CommonUI.getIconResourceForCategory
import com.raj.bankapp.presentation.theme.Grey40
import com.raj.bankapp.presentation.theme.Grey45
import com.raj.bankapp.presentation.theme.Grey50
import com.raj.bankapp.presentation.viewmodel.AccountViewModel
import com.raj.bankapp.util.DateUtil


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AccountInfoScreen(
    data: List<MainAccountListItem>,
    modifier: Modifier,
    navController: NavHostController,
    accountViewModel: AccountViewModel = hiltViewModel()
) {

    val isLoading by accountViewModel.isRefreshing.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    val accountHeaderTitle =
        data.find { it.type == ItemType.ACCOUNT_HEADER }?.data as AccountName

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(accountHeaderTitle.accountName)
                })
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { accountViewModel.getAccountData() }) {

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(data, itemContent = { item ->
                        when (item.type) {
                            ItemType.ACCOUNT_BALANCE -> BalanceDetails(
                                modifier,
                                item.data as AccountBalance
                            )

                            ItemType.ACCOUNT_NUMBER -> AccountDetails(
                                modifier,
                                item.data as AccountDetails
                            )

                            ItemType.TRANSACTION_HEADER -> TransactionHeader(
                                modifier,
                                item.data as String
                            )

                            ItemType.TRANSACTION_ITEM -> TransactionList(
                                modifier,
                                item.data as Transaction,
                                navController
                            )

                            ItemType.DIVIDER -> DividerAccount(
                                item.data as SectionDivider
                            )

                            else -> {
                                // do nothing
                            }
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun DividerAccount(sectionDivider: SectionDivider) {
    val paddingStart = if (sectionDivider.isTransactionList) 16 else 0
    Divider(
        color = Grey50,
        modifier = Modifier
            .fillMaxWidth()
            .width(1.dp)
            .padding(top = 8.dp, bottom = 8.dp, start = paddingStart.dp)
    )
}

@Composable
private fun BalanceDetails(modifier: Modifier, data: AccountBalance) {
    val availableBalanceDescription = stringResource(R.string.available) + data.available
    Column(modifier = modifier
        .semantics { contentDescription = availableBalanceDescription }) {
        Text(
            text = stringResource(R.string.available),
            modifier = modifier
                .padding(start = 16.dp, top = 16.dp),
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = stringResource(R.string.dollar_val, data.available),
            modifier = modifier
                .padding(start = 16.dp, top = 4.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
    val balanceDescription =
        stringResource(R.string.balance) + stringResource(R.string.dollar_val, data.balance)
    Row(modifier = Modifier.semantics { contentDescription = balanceDescription }) {
        Text(
            text = stringResource(R.string.balance),
            modifier = modifier
                .padding(start = 16.dp, top = 4.dp),
            color = Grey40,
            fontSize = 14.sp
        )
        Text(
            text = stringResource(R.string.dollar_val, data.balance),
            modifier = modifier
                .padding(start = 8.dp, top = 4.dp),
            fontSize = 14.sp,
            color = Grey45
        )
    }
    val pendingDescription =
        stringResource(R.string.pending) + stringResource(R.string.dollar_val, data.available)
    Row(modifier = Modifier.semantics { contentDescription = pendingDescription }) {
        Text(
            text = stringResource(R.string.pending),
            modifier = modifier
                .padding(start = 16.dp),
            color = Grey40,
            fontSize = 14.sp
        )
        Text(
            text = stringResource(R.string.dollar_val, data.available),
            modifier = modifier
                .padding(start = 8.dp),
            fontSize = 14.sp,
            color = Grey45
        )
    }

}

@Composable
private fun AccountDetails(modifier: Modifier, accountDetails: AccountDetails) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row {
            Text(
                text = stringResource(R.string.bsb),
                modifier = modifier.padding(start = 16.dp),
                color = Grey45,
                fontSize = 14.sp
            )
            Text(
                text = accountDetails.bsb,
                modifier = modifier.padding(start = 4.dp),
                fontSize = 14.sp,
                color = Grey40
            )
        }
        Row {
            Text(
                text = stringResource(R.string.account_number),
                modifier = modifier.padding(start = 4.dp),
                color = Grey45,
                fontSize = 14.sp
            )
            Text(
                text = accountDetails.accountNumber,
                modifier = modifier.padding(start = 4.dp),
                fontSize = 14.sp,
                color = Grey40
            )
        }
    }

}

@Composable
fun TransactionHeader(modifier: Modifier, date: String) {
    Row(
        modifier = modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val (formattedDate, daysDifference) = DateUtil.formatDateAndCalculateDifference(date)
        Text(
            text = formattedDate,
            modifier = modifier.padding(start = 16.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.days_ago, daysDifference),
            modifier = modifier.padding(start = 8.dp),
            fontSize = 14.sp,
            color = Grey45
        )
    }
}

@Composable
fun TransactionList(modifier: Modifier, transaction: Transaction, navigation: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigation.navigate(Screen.TransactionDetail.createRoute(transaction.id))
            }
            .padding(top = 16.dp, start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = getIconResourceForCategory(transaction.category)),
            contentDescription = transaction.category
        )
        Text(
            text = transaction.description,
            modifier = modifier
                .padding(start = 8.dp)
                .weight(1f),
            color = Grey40,
            fontSize = 14.sp,
            lineHeight = 15.sp
        )
        Text(
            text = formatAmount(transaction.amount),
            modifier = modifier
                .padding(start = 8.dp, end = 8.dp),
            fontSize = 16.sp
        )
    }
}