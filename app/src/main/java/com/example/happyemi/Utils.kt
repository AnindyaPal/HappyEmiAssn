package com.example.happyemi
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {

        const val API_KEY = "7858f3b19b854f9f8b6115745192301"
        fun getWeekDayName(date : String) : String {
            val df = SimpleDateFormat("yyyy-MM-dd")
            return SimpleDateFormat("EEEE").format(df.parse(date))
        }

        fun displayLongToast(s: String) {
            Toast.makeText(WeatherAppClass.getAppInstance(), s, Toast.LENGTH_LONG).show()
        }

        internal val permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
        internal val REQUEST_CODE_FOR_ALL = 1
    }
}