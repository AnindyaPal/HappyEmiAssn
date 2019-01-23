package com.example.happyemi.views.activity

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyemi.R
import com.example.happyemi.Utils
import com.example.happyemi.models.ForecastDay
import com.example.happyemi.viewModel.WeatherViewModel
import com.example.happyemi.views.adapters.RvForecastAdapter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_forecast.*
import java.util.*


class WeatherActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    var weatherViewModel : WeatherViewModel ?= null
    var rvForecastAdapter : RvForecastAdapter ?= null
    var forecastDays : MutableList<ForecastDay> = mutableListOf()
    var googleApiClient : GoogleApiClient ?= null

    var locationRequest : LocationRequest ?= null

    var city : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runtimePermission()
        showProgressDialog()
        initMembers()
        initLocationServices()
        setBottomSheetBehavior()
    }

    private fun setBottomSheetBehavior() {
        val bottomSheetBehavor = BottomSheetBehavior.from(bottomDialogForecast)
        bottomSheetBehavor.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bg.visibility = View.GONE
                }
            }
            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
                bg.visibility = View.VISIBLE
                bg.alpha = slideOffset
            }
        })
    }

    private fun showProgressDialog() {
        ivProgress.alpha = 1.0f
        ivProgress.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_360))
    }

    fun hideProgressDialog(){
        ivProgress.alpha = 0.0f
    }

    private fun initLocationServices() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

        googleApiClient?.connect()
    }

    private fun initMembers() {

        //setting recyclerView adapter
        rvForecastAdapter = RvForecastAdapter(forecastDays)
        rvForecast.adapter = rvForecastAdapter
        rvForecast.layoutManager = LinearLayoutManager(this)

        //view model init and observing it
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        weatherViewModel?.forecastLiveData?.observe(this, Observer {weather ->

            hideProgressDialog()
            if (weather != null) {
                run {
                    tvCurrentTemp.text = weather.current.temp_c.toInt().toString()
                    tvCity.text = city

                    for ( i : Int in 0 until weather.forecast.forecastday.size){
                        forecastDays.add(weather.forecast.forecastday[i])
                        if (i == 3) {
                            break
                        }
                    }

                    rvForecastAdapter?.notifyDataSetChanged()

                    val bottomSheetBehavor = BottomSheetBehavior.from(bottomDialogForecast)
                    bottomSheetBehavor.state = BottomSheetBehavior.STATE_EXPANDED
                }
            } else {
                showSnackbar()
            }

        })

    }

    override fun onConnected(p0: Bundle?) {
        locationRequest = LocationRequest.create()
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest?.interval = 1000

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Utils.permissions[1]) != PackageManager.PERMISSION_GRANTED)
                return
        }

        LocationServices.getFusedLocationProviderClient(this@WeatherActivity)
            .lastLocation.addOnSuccessListener {location->
            if (location != null) {
                val lat = location.latitude
                val lng = location.longitude

                val geoCoder = Geocoder(this, Locale.getDefault())
                val addresses = geoCoder.getFromLocation(lat, lng, 1)
                city = addresses[0].locality

                city?.let { weatherViewModel?.getForecast(Utils.API_KEY, it) }

            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun runtimePermission() {
        for (permission in Utils.permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, Utils.permissions, Utils.REQUEST_CODE_FOR_ALL)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Utils.REQUEST_CODE_FOR_ALL) run {
            if (grantResults.isNotEmpty()) {
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        //show toat
                        Utils.displayLongToast("Please grant the location permission!")
                    }
                }
            }
        }
    }

    private fun showSnackbar() {
        val snackbar = Snackbar.make(vRoot, "Please check internet connection" , Snackbar.LENGTH_INDEFINITE )
        snackbar.setAction("RETRY") { networkCheckAndProcess() }
        snackbar.setActionTextColor(ResourcesCompat.getColor(resources, R.color.colorAccent, null))
        snackbar.setActionTextColor(ContextCompat.getColor(this@WeatherActivity,R.color.green))
        snackbar.show()
    }

    private fun networkCheckAndProcess() {
        if (Utils.isNetworkAvailable(this@WeatherActivity)) {
            showProgressDialog()
            city?.let { weatherViewModel?.getForecast(Utils.API_KEY, it) }
        } else {
            showSnackbar()
        }
    }
}
