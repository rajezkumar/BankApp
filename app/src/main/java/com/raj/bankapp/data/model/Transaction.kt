package com.raj.bankapp.data.model


data class Transaction(
    val amount: String,
    val atmId: String?,
    val category: String,
    val description: String,
    val effectiveDate: String,
    val id: String,
    val isPending: Boolean
)