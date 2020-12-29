package com.martin.mt_weather.weather.json

data class Hour (

	val time_epoch : Int,
	val time : String,
	val temp_c : Int,
	val temp_f : Double,
	val condition : Condition,
	val wind_mph : Double,
	val wind_kph : Int,
	val wind_degree : Int,
	val wind_dir : String,
	val pressure_mb : Int,
	val pressure_in : Double,
	val precip_mm : Double,
	val precip_in : Double,
	val humidity : Int,
	val cloud : Int,
	val feelslike_c : Int,
	val feelslike_f : Double,
	val windchill_c : Int,
	val windchill_f : Double,
	val heatindex_c : Int,
	val heatindex_f : Double,
	val dewpoint_c : Int,
	val dewpoint_f : Double,
	val chance_of_rain : Int,
	val chance_of_snow : Int,
	val vis_km : Int,
	val vis_miles : Double,
	val gust_mph : Double,
	val gust_kph : Int
)