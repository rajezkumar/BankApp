package com.raj.bankapp.domain.model


data class MainAccountListItem(
    val type: ItemType,
    val data: Any?
)

data class AccountName(
    val accountName: String,
)

data class AccountBalance(
    val available: String,
    val balance: String,
)

data class AccountDetails(
    val accountNumber: String,
    val bsb: String
)

data class Transaction(
    val amount: String,
    val atmId: String?,
    val category: String,
    val description: String,
    val effectiveDate: String,
    val id: String,
    val isPending: Boolean
)

data class SectionDivider(
    val isTransactionList: Boolean
)