package com.example.bookreader.utils

import com.google.firebase.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun formatDate(timestamp: Timestamp): String {
    val date = DateFormat.getDateInstance()
        .format(timestamp.toDate())
        .toString().split(",")[0]

    return date
}