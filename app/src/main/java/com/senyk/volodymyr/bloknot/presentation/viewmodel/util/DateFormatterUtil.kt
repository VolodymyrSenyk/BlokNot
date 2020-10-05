package com.senyk.volodymyr.bloknot.presentation.viewmodel.util

import java.text.DateFormat
import java.util.*
import javax.inject.Inject

class DateFormatterUtil @Inject constructor() {

    companion object {

        @JvmStatic
        fun getShortInternationalDate(): String =
            getShortInternationalDate(System.currentTimeMillis())

        @JvmStatic
        fun getShortInternationalDate(date: Long): String {
            val dateString = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK).format(date)
            val timeString = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.UK).format(date)
            return "$dateString $timeString"
        }

        @JvmStatic
        fun getShortInternationalDate(date: Date): String = getShortInternationalDate(date.time)
    }
}
