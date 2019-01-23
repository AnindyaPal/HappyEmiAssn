package com.example.happyemi
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {

        const val API_KEY = "7858f3b19b854f9f8b6115745192301"
        fun getWeekDayName(epoc : Long) : String {
            return SimpleDateFormat("EEEE").format(Date(epoc))
        }

        fun displayLongToast(s: String) {
            Toast.makeText(WeatherAppClass.getAppInstance(), s, Toast.LENGTH_LONG).show()
        }

        internal val permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        internal val REQUEST_CODE_FOR_ALL = 1
    }
}