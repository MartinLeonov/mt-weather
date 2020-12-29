package com.martin.mt_weather.weather.json

data class Current (

	val last_updated_epoch : Int,
	val last_updated : String,
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
	val vis_km : Int,
	val vis_miles : Double,
	val uv : Int
)