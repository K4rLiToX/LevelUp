package com.carlosdiestro.levelup.core.ui

import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface TimeDelegate {
    fun todayInMillis() : Long
    fun todayInDate(format: DateFormat) : String
}

private class TimeDelegateImp() : TimeDelegate {
    override fun todayInMillis(): Long {
        return System.currentTimeMillis()
    }

    override fun todayInDate(format: DateFormat): String {
        val date = LocalDate.now()
        return date.format(DateTimeFormatter.ofPattern(format.value))
    }
}

sealed class DateFormat(val value: String) {
    object DDMMYYYY : DateFormat("dd/MM/yyyy")
    object MMMDD : DateFormat("MMM dd")
}
