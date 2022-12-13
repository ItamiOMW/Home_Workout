package com.example.homeworkout.utils

import java.time.LocalDate

class DateFormatterUtil {

    companion object {

        fun timeToLong(time: LocalDate): Long {
            return time.toEpochDay()
        }

        fun longToTime(long: Long): LocalDate {
            return LocalDate.ofEpochDay(long)
        }

        fun formatDateFromDatePicker(day: Int, month: Int, year: Int): String {
            //I ADDED 1 TO MONTH BECAUSE NUMBER OF FIRST MONTH IS 0
            return "${day}-${month + 1}-$year"
        }

    }
}