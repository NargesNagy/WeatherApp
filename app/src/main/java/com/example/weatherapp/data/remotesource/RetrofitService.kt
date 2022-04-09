package com.example.weatherapp.data.remotesource

import com.example.weatherapp.home.model.Weather
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
const val API_KEY = "fccb113f3db977a207025c87caa649c0"
//https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&lan=ar&units=metric&exclude=hourly,daily&appid=fccb113f3db977a207025c87caa649c0
interface RetrofitService {
    @GET("onecall?")//lat=33.44&lon=-94.04&exclude=hourly,daily&appid=${API_KEY}  //
    suspend fun getWeather(@Query("lat") lat:Double  , @Query ("lon") lon : Double  , @Query ("lan") lan : String , @Query ("units") units : String, @Query ("exclude") exclude : String = "minutely" , @Query("appid") appid : String = API_KEY ) : Response<Weather>

    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}