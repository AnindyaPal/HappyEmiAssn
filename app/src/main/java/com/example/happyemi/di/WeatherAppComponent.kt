package com.example.happyemi.di

import com.example.happyemi.repository.WeatherRepo
import com.example.happyemi.viewModel.WeatherViewModel
import dagger.Component

@WeatherAppScope
@Component(modules = [ApiRepositoryModule::class])
interface WeatherAppComponent {
    public fun getWeatherRepo() : WeatherRepo
    public fun injectWeatherActivity(weatherViewModel: WeatherViewModel)
}