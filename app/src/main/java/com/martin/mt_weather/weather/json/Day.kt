package com.martin.mt_weather.weather.json

data class Day (

	val maxtemp_c : Int,
	val maxtemp_f : Double,
	val mintemp_c : Int,
	val mintemp_f : Double,
	val avgtemp_c : Int,
	val avgtemp_f : Double,
	val condition : Condition,
	val uv : Int
)