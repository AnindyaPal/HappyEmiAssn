package com.example.happyemi.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.happyemi.WeatherAppClass
import com.example.happyemi.models.Weather
import com.example.happyemi.repository.WeatherRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class WeatherViewModel() : ViewModel(), CoroutineScope{

    @Inject
    internal lateinit var weatherRepo: WeatherRepo

    var job : Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    var currentLiveData : MutableLiveData<Weather> = MutableLiveData()
    var forecastLiveData : MutableLiveData<Weather> = MutableLiveData()

    init {
        WeatherAppClass.getAppInstance()?.getappComponent()?.injectWeatherActivity(this)
    }

    fun getForecast( apiKey : String, city : String){
        launch { performGetForecast(apiKey, city) }
    }

    suspend fun performGetForecast( apiKey : String, city : String){
        try {
            var weather = weatherRepo.getForecastWeather(apiKey = apiKey, city = city).await()
            forecastLiveData.value = weather
        }
        catch (e : Exception) {
            forecastLiveData.value = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}