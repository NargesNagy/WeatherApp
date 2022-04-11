package com.example.weatherapp.data

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.local.FavoriteDao
import com.example.weatherapp.favorite.model.FavoriteModel

class Repo2(private  val favoriteDao: FavoriteDao){

    fun getAllFavorites(): LiveData<List<FavoriteModel>> = favoriteDao.getAllFavoriteCites()
    suspend fun insertCity(favorite: FavoriteModel) { favoriteDao.insertCity(favorite) }
    suspend fun deleteCity(favorite: FavoriteModel) { favoriteDao.deleteCity(favorite) }

}