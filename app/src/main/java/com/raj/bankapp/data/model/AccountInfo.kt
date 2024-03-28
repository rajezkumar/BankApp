package com.raj.bankapp.data.model


data class AccountInfo(
    val account: Account,
    val atms: List<Atm>,
    val transactions: List<Transaction>
)