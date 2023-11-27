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

fun createDateString(year : Int, month : Int, day : Int): String{
    val monthString = if(month < 10){
        "0$month"
    }
    else{
        month.toString()
    }

    val dayString = if(day < 10){
        "0$day"
    }
    else{
        day.toString()
    }

    return "$year-$monthString-$dayString"
}
