package com.example.weatherapp.favorite.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite")
data class FavoriteModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo( name = "areaname")
    val  areaname : String? ,
    @ColumnInfo( name = "latitude")
    val  latitude : Double? ,
    @ColumnInfo( name = "longtude")
    val  longtude : Double? )