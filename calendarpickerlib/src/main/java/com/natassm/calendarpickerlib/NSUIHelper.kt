package com.natassm.calendarpickerlib

import java.text.SimpleDateFormat
import java.util.*

object NSUIHelper {
    fun convertLongToPatternTime(pattern: String, time: Long): String {
        val dateFormat = SimpleDateFormat(pattern, Locale("id", "ID"))
        return dateFormat.format(Date(time))
    }
}