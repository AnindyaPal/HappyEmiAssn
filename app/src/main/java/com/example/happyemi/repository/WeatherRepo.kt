package com.example.happyemi.repository

import com.example.happyemi.models.Weather
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherRepo {
    @GET("/v1/current.json?key={apiKey}&q={city}")
    fun getCurrentWeather(@Path("apiKey") apiKey : String,@Path("city") city : String) :Deferred<Weather>

    @GET("/v1/forecast.json?key={apiKey}&q={city}")
    fun getForecastWeather(@Path("apiKey") apiKey : String,@Path("city") city : String) :Deferred<Weather>
}