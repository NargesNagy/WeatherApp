package com.example.weatherapp.favorite.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.Repo2
import com.example.weatherapp.data.Repository
import com.example.weatherapp.home.viewmodel.MyViewModel
/*
class FavoriteViewModelFactory constructor(private var context: Context): ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            FavoriteViewModel( this.context ) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
*/
