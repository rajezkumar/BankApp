package com.raj.bankapp.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtil {

    fun formatDateAndCalculateDifference(dateStr: String): Pair<String, Int> {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE dd MMM", Locale.getDefault())
        val date = inputFormat.parse(dateStr)
        val formattedDate = outputFormat.format(date)
        val currentDate = Calendar.getInstance()
        val dateToCompare = Calendar.getInstance()
        dateToCompare.time = date
        val diffInMillis = currentDate.timeInMillis - dateToCompare.timeInMillis
        val daysDifference = (diffInMillis / (24 * 60 * 60 * 1000)).toInt()
        return Pair(formattedDate, daysDifference)
    }
}