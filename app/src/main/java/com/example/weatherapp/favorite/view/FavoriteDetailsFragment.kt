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

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var lattitude : Double = 31.1926745
    var longtude : Double = 29.9245787


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        binding = FragmentFavoriteDetailsBinding.inflate(LayoutInflater.from(context) , container , false)//,container , false)
        //setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())


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


        getCurrentLocation()
        viewModel.getAllMovies(lattitude , longtude ,"en" , "metric")//

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


    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationIsEnabled()) {
                getLocations()
            } else {
                // Toast.makeText(this, "Turn on Location", Toast.LENGTH_SHORT).show()
                enableLocationSettings()
            }
        } else {
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_REQUEST_ACESS_LOCATION
        )
    }

    companion object {
        private const val PERMISSION_REQUEST_ACESS_LOCATION = 100
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACESS_LOCATION) {
            if (grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()

            } else {
                //Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("ServiceCast")
    private fun isLocationIsEnabled(): Boolean {
        val locationManager : LocationManager = getActivity()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // val locationManager: LocationManager = //getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun enableLocationSettings() {
        val settingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(settingIntent)
    }

    @SuppressLint("MissingPermission")
    private fun getLocations() {

        fusedLocationProviderClient.lastLocation?.addOnCompleteListener {
            //@NonNull
            val location: Location? = it.getResult()
            if (location == null) {
                requestNewLocationData()
            } else it.apply {
                lattitude = location.latitude
                longtude = location.longitude
                Log.i("TAG", "getLocations: ${lattitude} ggggggggggg ${longtude}")

                viewModel.getAllMovies(lattitude , longtude ,"en" , "metric")//

                //  text.text = "Lattitude $lat  and Longtude $lon"
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        // initialize locationrequest
        // object with aproparate methods
        val mlocationRequest = LocationRequest()
        mlocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mlocationRequest.interval = 5
        mlocationRequest.fastestInterval = 0
        mlocationRequest.numUpdates = 1

        // setting locationrequest
        // on fusedlocationclient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.requestLocationUpdates(
            mlocationRequest, mLocationCallBack,
            Looper.myLooper()!!
        )
    }

    private val mLocationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val mLastLocation = locationResult.lastLocation
            lattitude = mLastLocation.latitude
            longtude = mLastLocation.longitude

            Log.i("TAG", "onLocationResult: ${lattitude } hhh ${longtude}" )
            viewModel.getAllMovies(lattitude , longtude ,"en" , "metric")//

            // text.setText("Latitude : " + mLastLocation.latitude)
            //text.setText("Longitude : " + mLastLocation.longitude)
        }
    }

}