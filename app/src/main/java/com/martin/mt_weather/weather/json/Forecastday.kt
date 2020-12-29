package com.martin.mt_weather.weather.json

data class Forecastday (

    val date : String,
    val date_epoch : Int,
    val day : Day,
    val astro : Astro,
    val hour : List<Hour>
)