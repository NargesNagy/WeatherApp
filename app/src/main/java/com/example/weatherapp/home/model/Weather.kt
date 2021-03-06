package com.example.weatherapp.home.model

data class Weather(val lat: Double ,
                   val lon: Double ,
                   val timezone : String ,
                   val timezone_offset : Int ,
                   val current: Current,
                   val minutely: List<Minutely>? ,
                   val hourly : List<Hourly>? ,
                   val daily : List<Daily>? ,
                   val alerts : List<Alert> )
