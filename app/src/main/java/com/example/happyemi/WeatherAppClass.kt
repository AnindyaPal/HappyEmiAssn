package com.example.happyemi

import android.app.Application
import com.example.happyemi.di.ApiRepositoryModule
import com.example.happyemi.di.DaggerWeatherAppComponent
import com.example.happyemi.di.WeatherAppComponent

class WeatherAppClass : Application() {

    var appComponent : WeatherAppComponent ?= null

    companion object {
        var APPINSTANCE: WeatherAppClass ?= null
        fun getAppInstance() : WeatherAppClass? {
            return APPINSTANCE
        }
    }

    override fun onCreate() {
        super.onCreate()
        APPINSTANCE = this
        appComponent = DaggerWeatherAppComponent.builder()
            .apiRepositoryModule(ApiRepositoryModule())
            .build()
    }

    fun getappComponent() : WeatherAppComponent? {
        return appComponent
    }
}