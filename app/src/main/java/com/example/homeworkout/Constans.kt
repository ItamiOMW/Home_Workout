package com.example.homeworkout

const val UNKNOWN_ID = 0

const val UNKNOWN_IF_WORKOUT_COMPLETED = false

const val MILLIS_IN_SECOND = 999L

const val SECONDS_IN_HOUR = 3600

const val SECONDS_IN_MINUTE = 60

const val BAR_CHART_ANIMATION_DURATION = 1000

fun formatDateFromDatePicker(day: Int, month: Int, year: Int): String {
    //I ADDED 1 TO MONTH BECAUSE NUMBER OF FIRST MONTH IS 0
    return "${day}-${month + 1}-$year"
}

fun formatDateFromCalendarView(day: Int, month: Int, year: Int): String {
    //I ADDED 1 TO MONTH BECAUSE NUMBER OF FIRST MONTH IS 0
    return "${day}-${month + 1}-$year"
}

fun formatDate(day: Int, month: Int, year: Int): String {
    return "${day}-${month}-$year"
}