package ru.churkin.confectioners_organizer.date

import java.text.SimpleDateFormat
import java.util.*

fun Date.format(pattern: String = "dd.MM.yyyy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun String.parseDate(pattern: String = "dd.MM.yyyy"): Date {
    return SimpleDateFormat(pattern).parse(this)
}