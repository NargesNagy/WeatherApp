package com.example.weatherapp.home.model

data class Hourly( val dt: Long ,
                   val temp: Double,
                   val feels_like: Double,
                   val pressure: Int ,
                   val humidity: Int ,
                   val dew_point: Double,
                   val uvi: Double,
                   val clouds: Int,
                   val visibility: Int,
                   val wind_speed: Double ,
                   val wind_deg: Int,
                   val wind_gust: Double,
                   val weather: List<WeatherItem>,
                   val pop: Double )
