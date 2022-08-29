package com.carlosdiestro.levelup.core.ui.managers

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object TimeManager {
    sealed class DateFormat(val value: String) {
        object DDMMYYYY : DateFormat("dd/MM/yyyy")
    }

    fun now(): String = LocalDate.now().format(
        DateTimeFormatter.ofPattern(DateFormat.DDMMYYYY.value)
    )

    fun yesterday(): String = LocalDate.now().minusDays(1).format(
        DateTimeFormatter.ofPattern(DateFormat.DDMMYYYY.value)
    )

    fun toMillis(date: String): Long = SimpleDateFormat(DateFormat.DDMMYYYY.value, Locale.getDefault()).parse(date)!!.time

    fun toText(date: Long) : String = SimpleDateFormat(DateFormat.DDMMYYYY.value, Locale.getDefault()).format(Date(date))
}
