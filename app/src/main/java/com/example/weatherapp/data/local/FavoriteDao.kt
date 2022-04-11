package com.example.weatherapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.favorite.model.FavoriteModel

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCity(city: FavoriteModel)

    @Delete
    suspend fun deleteCity(city: FavoriteModel)

    @Query("SELECT * From Favorite")
    fun getAllFavoriteCites(): LiveData<List<FavoriteModel>>

}
