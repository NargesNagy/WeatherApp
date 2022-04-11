package com.example.weatherapp.favorite.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.Repository
import com.example.weatherapp.data.remotesource.RetrofitService
import com.example.weatherapp.databinding.FragmentFavoriteDetailsBinding
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.home.model.Daily
import com.example.weatherapp.home.model.Hourly
import com.example.weatherapp.home.view.DailyRecycleAdapter
import com.example.weatherapp.home.view.HoursRecycleAdapter
import com.example.weatherapp.home.viewmodel.MyViewModel
import com.example.weatherapp.home.viewmodel.ViewModelFactory
import com.google.android.gms.location.*
import java.text.SimpleDateFormat
import java.util.*


class FavoriteDetailsFragment : Fragment() {

    lateinit var viewModel: MyViewModel
    lateinit var binding : FragmentFavoriteDetailsBinding

    lateinit var hoursRecycleView : RecyclerView;
    lateinit var hoursRecycleViewAdapter : HoursRecycleAdapter;
    lateinit var hoursList : List<Hourly>

    lateinit var dailyRecycleView : RecyclerView;
    lateinit var dailyRecycleViewAdapter : DailyRecycleAdapter;
    lateinit var dailyList : List<Daily>


    var lattitude : Double = 31.1926745
    var longtude : Double = 29.9245787
    var language : String = "en"
    var unites : String = "metric"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // get latitude and longtude from favorite fragment
        val jj = Bundle()
        jj.get("latt")

        var args = this.arguments
        lattitude = args?.get("latitude") as Double
        longtude = args?.get("longtude") as Double

        // get language and unites from  shared
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        language = sharedPreferences.getString("languages", "").toString()
        unites = sharedPreferences.getString("tempretures" , "").toString()

        binding = FragmentFavoriteDetailsBinding.inflate(LayoutInflater.from(context) , container , false)//,container , false)

        val retrofitService = RetrofitService.getInstance()
        val mainRepository = Repository(retrofitService)


        viewModel = ViewModelProvider(this, ViewModelFactory(mainRepository)).get(MyViewModel::class.java)


        viewModel._response.observe(requireActivity(), {
            /*
            Weather->
        binding.apply {
            binding.dateHomeTextfragment.text = Weather.timezone
        }

        */

            val weather = it
            val timezone = weather.timezone

            binding.favoriteDetailsCountryNameHomeText.text = timezone


            Log.i("TAG", "onCreateView: ${weather.current.weather?.get(0)?.icon}")

            val formatedDate: String = SimpleDateFormat("EEE, d MMM yyyy ", Locale.ENGLISH).format(
                Date()
            )
            binding.favoriteDetailsDateHomeText.text = formatedDate
            binding.favoriteDetailsTemperatureHomeText.text = weather.current.temp.toInt().toString() +"°"

            var icon = weather.current.weather?.get(0)?.icon
            when (icon){
                "01d" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.cloud_sun2)
                "02d" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.cloud2)
                "03d" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.blackcloud_lighting)
                "04d" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.cloud2)
                "09d" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.cloud_rain)
                "10d" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.cloud_sun2)
                "11d" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.clouds__rain_sun)
                "13d" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.clouds_sun)
                "50d" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.darkcloud_rain)
                "01n" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.stormy)
                "02n" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.cloud2)
                "03n" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.cloud_sun2)
                "04n" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.cloud2)
                "09n" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.cloud_lighting)
                "10n" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.stormy)
                "11n" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.stormy)
                "13n" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.rain)
                "50n" -> binding.favoriteDetailsShowimageView.setImageResource(R.drawable.rain)

            }

            hoursList = weather?.hourly ?: emptyList()
            dailyList = weather?.daily?: emptyList()

            binding.favoriteDetailsPressurstext.text = weather.daily?.get(0)?.pressure.toString() + " hpa"
            binding.favoriteDetailsHumiditytext.text=weather.daily?.get(0)?.humidity.toString() + " %"
            binding.favoriteDetailsWindtext.text=weather.daily?.get(0)?.wind_speed.toString() + " m/s"
            binding.favoriteDetailsCloudtext.text=weather.daily?.get(0)?.clouds.toString() + " %"
            binding.favoriteDetailsUltraviolittext.text=weather.current?.uvi.toString() + ""
            binding.favoriteDetailsVisibilitytext.text=weather.current?.visibility.toString() + " m"


            gethoursRecyleview()
            getDailyRecyleview()
        })

        viewModel.errorMessage.observe(requireActivity(), {
            //Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })


        viewModel.getAllMovies(lattitude , longtude ,language , unites , "minutely" , "fccb113f3db977a207025c87caa649c0")//

        return binding.root
    }

    private fun gethoursRecyleview() {
        hoursRecycleView = binding.favoriteDetailsHoursRecycleView
        val layoutManager = LinearLayoutManager(requireContext() )
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        hoursRecycleViewAdapter = HoursRecycleAdapter( hoursList , requireContext())
        hoursRecycleView.adapter = hoursRecycleViewAdapter
        hoursRecycleView.layoutManager = layoutManager
    }

    private fun getDailyRecyleview() {
        dailyRecycleView = binding.favoriteDetailsDailyRecycleView
        val layoutManager = LinearLayoutManager(requireContext() )
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        dailyRecycleViewAdapter = DailyRecycleAdapter(dailyList , requireContext())
        dailyRecycleView.adapter = dailyRecycleViewAdapter
        dailyRecycleView.layoutManager = layoutManager
    }

    override fun onPause() {
        super.onPause()
        binding.favoriteDetailsCountryNameHomeText.visibility = View.GONE
    }
}