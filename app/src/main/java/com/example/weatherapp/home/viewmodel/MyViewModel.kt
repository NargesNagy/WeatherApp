package com.example.weatherapp.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.Repository
import com.example.weatherapp.home.model.Weather
import kotlinx.coroutines.*

class MyViewModel constructor(private val mainRepository: Repository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val movieList = MutableLiveData<List<Weather>>()
    var _response = MutableLiveData<Weather>()
    val WeatherResponse: LiveData<Weather>
        get() = _response




    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getAllMovies(lat : Double , lon : Double , lan : String , unites : String) { //
        job = CoroutineScope(Dispatchers.IO ).launch {
            val response = mainRepository.getWeather(lat , lon , lan , unites )//, lan , unites
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    _response.postValue(response.body())
                    // loading.value = false
                } else {
                    onError("Error rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr: ${response?.message()} ")
                }
            }
        }

    }

    private fun onError(message: String) {
        errorMessage.value = message
        //  loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}