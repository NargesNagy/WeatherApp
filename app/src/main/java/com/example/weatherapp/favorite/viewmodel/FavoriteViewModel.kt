package com.example.weatherapp.favorite.viewmodel
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.Repo2
import com.example.weatherapp.data.Repository
import com.example.weatherapp.data.local.AppDataBase
import com.example.weatherapp.favorite.model.FavoriteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel (
    application: Application
) : AndroidViewModel(application) {

    val favorites: LiveData<List<FavoriteModel>>
    val repository: Repo2

    init {
        val dao = AppDataBase.getInstance(application).getFavorieCityDao()
        repository = Repo2(dao)
        favorites = repository.getAllFavorites()
    }

    fun insertCity(favorite: FavoriteModel) =
        viewModelScope.launch(Dispatchers.IO) { repository.insertCity(favorite) }

    fun deleteCity(favorite: FavoriteModel) =
        viewModelScope.launch(Dispatchers.IO) { repository.deleteCity(favorite) }


}