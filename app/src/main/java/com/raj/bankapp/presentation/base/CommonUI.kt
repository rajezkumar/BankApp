package com.raj.bankapp.presentation.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.raj.bankapp.R
import kotlin.math.absoluteValue

object CommonUI {

    @Composable
    fun formatAmount(amount: String): String {
        val amountInt = amount.toDouble()
        val formattedAmount = if (amountInt < 0) {
            stringResource(R.string.dollar_val_negative, amountInt.absoluteValue)
        } else {
            stringResource(R.string.dollar_val_positive, amountInt)
        }
        return formattedAmount
    }

    @Composable
    fun getIconResourceForCategory(category: String): Int {
        return when (category) {
            "business" -> R.drawable.icon_category_business
            "cards" -> R.drawable.icon_category_cards
            "cash" -> R.drawable.icon_category_cash
            "entertainment" -> R.drawable.icon_category_categories
            "eatingOut" -> R.drawable.icon_category_eating_out
            "education" -> R.drawable.icon_category_education
            "groceries" -> R.drawable.icon_category_groceries
            "shopping" -> R.drawable.icon_category_shopping
            "transport" -> R.drawable.icon_category_transportation
            "categories" -> R.drawable.icon_category_travel
            "uncategorised" -> R.drawable.icon_category_uncategorised
            else -> R.drawable.ic_launcher_background
        }
    }
}