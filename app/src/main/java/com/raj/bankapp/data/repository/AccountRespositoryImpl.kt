package com.raj.bankapp.data.repository

import com.raj.bankapp.util.ApiUtil
import com.raj.bankapp.util.Resource
import com.raj.bankapp.data.api.AccountApi
import com.raj.bankapp.data.model.AccountInfo
import com.raj.bankapp.domain.repository.AccountRepository
import com.raj.bankapp.domain.model.AccountBalance
import com.raj.bankapp.domain.model.AccountDetails
import com.raj.bankapp.domain.model.AccountName
import com.raj.bankapp.domain.model.ItemType
import com.raj.bankapp.domain.model.MainAccountListItem
import com.raj.bankapp.domain.model.SectionDivider
import com.raj.bankapp.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AccountRespositoryImpl @Inject constructor(
    private val accountApi: AccountApi
) : AccountRepository {
    override suspend fun getAccountData(): Flow<Resource<List<MainAccountListItem>>> = flow {
        val accountInfo = ApiUtil.parseResponse(accountApi.getAccount("")) {
            it.getMutableListAccount()
        }
        emit(accountInfo)
    }

    // Converts AccountInfo into a list of MainAccountListItem objects for structured UI display.
    private fun AccountInfo?.getMutableListAccount(): List<MainAccountListItem> {
        return this?.let { accountData ->
            val groupedTransactions = transactions.groupBy { it.effectiveDate }
            val accountMainList = mutableListOf<MainAccountListItem>()

            accountMainList.add(
                MainAccountListItem(
                    ItemType.ACCOUNT_HEADER,
                    AccountName(accountData.account.accountName)
                )
            )
            accountMainList.add(
                MainAccountListItem(
                    ItemType.ACCOUNT_BALANCE,
                    AccountBalance(accountData.account.available, accountData.account.balance)
                )
            )
            accountMainList.add(
                MainAccountListItem(
                    ItemType.DIVIDER, SectionDivider(isTransactionList = false)
                )
            )
            accountMainList.add(
                MainAccountListItem(
                    ItemType.ACCOUNT_NUMBER,
                    AccountDetails(accountData.account.accountNumber, accountData.account.bsb)
                )
            )
            accountMainList.add(
                MainAccountListItem(
                    ItemType.DIVIDER, SectionDivider(isTransactionList = false)
                )
            )

            groupedTransactions.forEach { (key, value) ->
                accountMainList.add(MainAccountListItem(ItemType.TRANSACTION_HEADER, key))
                accountMainList.addAll(value.map { transaction ->
                    MainAccountListItem(
                        ItemType.TRANSACTION_ITEM,
                        Transaction(
                            transaction.amount,
                            transaction.atmId,
                            transaction.category,
                            transaction.description,
                            transaction.effectiveDate,
                            transaction.id,
                            transaction.isPending
                        )
                    )
                })
                accountMainList.add(
                    MainAccountListItem(
                        ItemType.DIVIDER, SectionDivider(isTransactionList = true)
                    )
                )
            }
            accountMainList
        } ?: emptyList()
    }
}