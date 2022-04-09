package com.example.weatherapp.data

import com.example.weatherapp.data.remotesource.RetrofitService

class Repository constructor(private val retrofitService: RetrofitService) { //

    suspend fun getWeather(lat : Double , lon : Double, lan : String , unites : String ) = retrofitService.getWeather(lat , lon , lan , unites )

}