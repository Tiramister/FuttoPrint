package com.example.futtoprint.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDateTime(): String {
    val df = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
    return df.format(Date(this))
}
