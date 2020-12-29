package com.martin.mt_weather.weather

import com.martin.mt_weather.weather.json.Weather
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL =
    "http://192.168.1.151:6655"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MarsApiService {
    @GET("/weather")
    suspend fun getWeather(@Query("city") city: String, @Query("days") days: String): Weather
}

object WeatherAPI {
    val retrofitService: MarsApiService by lazy { retrofit.create(MarsApiService::class.java) }
}
