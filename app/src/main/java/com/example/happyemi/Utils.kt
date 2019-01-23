package com.example.happyemi
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun getWeekDayName(epoc : Long) : String {
            return SimpleDateFormat("EEEE").format(Date(epoc))
        }
    }
}