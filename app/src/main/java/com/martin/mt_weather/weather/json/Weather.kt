package com.martin.mt_weather.weather.json

data class Weather (

	val location : Location,
	val current : Current,
	val forecast : Forecast,
)