package by.esas.forgetmenot.utils

import java.text.SimpleDateFormat
import java.util.*

internal fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

internal fun getCurrentDateTime(): Date = Calendar.getInstance().time