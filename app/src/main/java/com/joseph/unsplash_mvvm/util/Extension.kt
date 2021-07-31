package com.joseph.unsplash_mvvm.util

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

fun String.convertToSimpleDateFormat(): String {
    val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat("M-D-YY", Locale.ENGLISH)
    } else {
        return ""
    }
    return date.parse(this).toString()
}