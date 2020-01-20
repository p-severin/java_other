package com.example.patry.kotlinweather.models
import com.google.gson.annotations.SerializedName


/**
 * Created by patry on 11.01.2018.
 */

data class MainWeather(
		@SerializedName("coord") val coord: Coord,
		@SerializedName("weather") val weather: List<Weather>,
		@SerializedName("base") val base: String,
		@SerializedName("main") val main: Main,
		@SerializedName("wind") val wind: Wind,
		@SerializedName("clouds") val clouds: Clouds,
		@SerializedName("dt") val dt: Int,
		@SerializedName("sys") val sys: Sys,
		@SerializedName("id") val id: Int,
		@SerializedName("name") val name: String,
		@SerializedName("cod") val cod: Int
)

data class Sys(
		@SerializedName("message") val message: Double,
		@SerializedName("country") val country: String,
		@SerializedName("sunrise") val sunrise: Int,
		@SerializedName("sunset") val sunset: Int
)

data class Wind(
		@SerializedName("speed") val speed: Double,
		@SerializedName("deg") val deg: Int
)

data class Weather(
		@SerializedName("id") val id: Int,
		@SerializedName("main") val main: String,
		@SerializedName("description") val description: String,
		@SerializedName("icon") val icon: String
)

data class Clouds(
		@SerializedName("all") val all: Int
)

data class Main(
		@SerializedName("temp") val temp: Double,
		@SerializedName("pressure") val pressure: Double,
		@SerializedName("humidity") val humidity: Int,
		@SerializedName("temp_min") val tempMin: Double,
		@SerializedName("temp_max") val tempMax: Double,
		@SerializedName("sea_level") val seaLevel: Double,
		@SerializedName("grnd_level") val grndLevel: Double
)

data class Coord(
		@SerializedName("lon") val lon: Double,
		@SerializedName("lat") val lat: Double
)