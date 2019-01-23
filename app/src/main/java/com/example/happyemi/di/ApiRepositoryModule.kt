package com.example.happyemi.di

import com.example.happyemi.repository.WeatherRepo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class ApiRepositoryModule {

    companion object {
        val API_BASE_URL : String = "http://api.apixu.com/"
    }
    @Provides
    @WeatherAppScope
    fun getLoggerIntercepter(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @WeatherAppScope
    fun getGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @WeatherAppScope
    fun getOkHttpBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
    }

    @Provides
    @WeatherAppScope
    fun getRetrofitBuilder(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Provides
    @WeatherAppScope
    fun createV4Services(httpClient: OkHttpClient.Builder, logging: HttpLoggingInterceptor,
                         builder: Retrofit.Builder): WeatherRepo {
        if (!httpClient.interceptors().isEmpty()) {
            httpClient.interceptors().clear()
        }

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader("Content-Type", "application/json")
                .method(original.method(), original.body())

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        httpClient.addInterceptor(logging)
        val retrofit = builder.client(httpClient.build())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        return retrofit.create(WeatherRepo::class.java)
    }
}