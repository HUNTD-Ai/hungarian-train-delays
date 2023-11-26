package com.huntdai.hungariantraindelays.utils

import android.content.Context
import android.graphics.Color
import com.huntdai.hungariantraindelays.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun getMonthNameFromTimestamp(timestamp: Double): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp.toLong()

    // Define the desired date format
    val dateFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)

    return dateFormat.format(calendar.time)
}

fun getCurrentTextcolor(context: Context): Int {
    val typedArray = context.obtainStyledAttributes(
        R.style.Base_Theme_HungarianTrainDelays,
        intArrayOf(android.R.attr.textColor)
    )
    val textColor = typedArray.getColor(0, Color.BLACK)
    typedArray.recycle()
    return textColor
}