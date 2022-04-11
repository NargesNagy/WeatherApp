package com.example.weatherapp.favorite.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite")
data class FavoriteModel(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo( name = "areaname")
    val  areaname : String? ,
    @ColumnInfo( name = "latitude")
    val  latitude : Double? ,
    @ColumnInfo( name = "longtude")
    val  longtude : Double? )