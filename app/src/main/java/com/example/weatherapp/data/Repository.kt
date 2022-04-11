package com.example.weatherapp.data

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.local.FavoriteDao
import com.example.weatherapp.data.remotesource.API_KEY
import com.example.weatherapp.data.remotesource.RetrofitService
import com.example.weatherapp.data.remotesource.RetrofitService.Companion.retrofitService
import com.example.weatherapp.favorite.model.FavoriteModel

class Repository constructor() { //

    constructor( retrofitService: RetrofitService) : this()
    constructor( favoriteDao: FavoriteDao) : this()



    suspend fun getWeather(lat : Double , lon : Double, lan : String , unites : String , exclude : String  , appid : String ) =
        retrofitService?.getWeather(lat , lon , lan , unites , exclude , appid )






}