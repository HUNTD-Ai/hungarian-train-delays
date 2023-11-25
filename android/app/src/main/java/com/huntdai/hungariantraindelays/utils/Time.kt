package com.huntdai.hungariantraindelays.utils

import java.util.Calendar

fun getTodaysDate(): Calendar {
    val today: Calendar = Calendar.getInstance()
    today.set(Calendar.HOUR_OF_DAY, 0)
    today.set(Calendar.MINUTE, 0)
    today.set(Calendar.SECOND, 0)
    today.set(Calendar.MILLISECOND, 0)
    return today
}