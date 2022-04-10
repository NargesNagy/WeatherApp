package com.example.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.favorite.model.FavoriteModel

@Database (entities = [FavoriteModel::class] , version = 1 )
abstract class AppDataBase : RoomDatabase(){

    abstract fun getFavorieCityDao() : FavoriteDao

    companion object{
        @Volatile
        private var INSTANCE: AppDataBase? = null

        private const val DB_NAME = "favoritecites_database.db"

        fun getInstance(context: Context) : AppDataBase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext , AppDataBase::class.java , DB_NAME).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}