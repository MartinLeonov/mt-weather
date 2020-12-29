package com.martin.mt_weather.weather.json

data class Location (

	val name : String,
	val region : String,
	val country : String,
	val lat : Double,
	val lon : Double,
	val tz_id : String,
	val localtime_epoch : Int,
	val localtime : String
)