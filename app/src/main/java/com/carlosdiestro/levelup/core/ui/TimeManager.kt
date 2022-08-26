package com.carlosdiestro.levelup.core.ui

import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface TimeManager {
    fun todayInMillis() : Long
    fun todayInDate(format: DateFormat) : String

    sealed class DateFormat(val value: String) {
        object DDMMYYYY : DateFormat("dd/MM/yyyy")
        object MMMDD : DateFormat("MMM dd")
    }
}

private class TimeManagerImpl() : TimeManager {
    override fun todayInMillis(): Long {
        return System.currentTimeMillis()
    }

    override fun todayInDate(format: TimeManager.DateFormat): String {
        val date = LocalDate.now()
        return date.format(DateTimeFormatter.ofPattern(format.value))
    }
}
