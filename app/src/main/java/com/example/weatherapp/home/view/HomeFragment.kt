package com.example.weatherapp.home.view

//import com.example.weatherapp.databinding.FragmentHomeBinding
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.Repository
import com.example.weatherapp.data.remotesource.RetrofitService
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.home.model.Daily
import com.example.weatherapp.home.model.Hourly
import com.example.weatherapp.home.viewmodel.MyViewModel
import com.example.weatherapp.home.viewmodel.ViewModelFactory
import com.google.android.gms.location.*
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    lateinit var viewModel: MyViewModel
    lateinit var binding: FragmentHomeBinding

    lateinit var hoursRecycleView: RecyclerView;
    lateinit var hoursRecycleViewAdapter: HoursRecycleAdapter;
    lateinit var hoursList: List<Hourly>

    lateinit var dailyRecycleView: RecyclerView;
    lateinit var dailyRecycleViewAdapter: DailyRecycleAdapter;
    lateinit var dailyList: List<Daily>

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var lattitude: Double = 31.1926745
    var longtude: Double = 29.9245787
    var language: String = "en"
    var unites: String = "metric"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        language = sharedPreferences.getString("languages", "").toString()
        unites = sharedPreferences.getString("tempretures", "").toString()

        // get the language and apply iy
        val configuration: Configuration = requireContext().resources.configuration
        var locale = Locale(language)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)

        } else {
            configuration.locale = locale
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activity?.getApplicationContext()?.createConfigurationContext(configuration);
            resources.updateConfiguration(configuration, resources.displayMetrics)
            binding = FragmentHomeBinding.inflate(LayoutInflater.from(context), container, false)//,container , false)

        } else {
            binding = FragmentHomeBinding.inflate(LayoutInflater.from(context), container, false)//,container , false)
        }


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

            binding.countryNameHomeText.text = timezone

            Log.i("TAG", "onCreateView: ${weather.current.weather?.get(0)?.icon}")

            val formatedDate: String = SimpleDateFormat("EEE, d MMM yyyy ", Locale.ENGLISH).format(Date())
            binding.dateHomeText.text = formatedDate
            binding.temperatureHomeText.text = weather.current.temp.toInt().toString() + "°"

            var icon = weather.current.weather?.get(0)?.icon
            when (icon) {
                "01d" -> binding.showimageView.setImageResource(R.drawable.cloud_sun2)
                "02d" -> binding.showimageView.setImageResource(R.drawable.cloud2)
                "03d" -> binding.showimageView.setImageResource(R.drawable.blackcloud_lighting)
                "04d" -> binding.showimageView.setImageResource(R.drawable.cloud2)
                "09d" -> binding.showimageView.setImageResource(R.drawable.cloud_rain)
                "10d" -> binding.showimageView.setImageResource(R.drawable.cloud_sun2)
                "11d" -> binding.showimageView.setImageResource(R.drawable.clouds__rain_sun)
                "13d" -> binding.showimageView.setImageResource(R.drawable.clouds_sun)
                "50d" -> binding.showimageView.setImageResource(R.drawable.darkcloud_rain)
                "01n" -> binding.showimageView.setImageResource(R.drawable.stormy)
                "02n" -> binding.showimageView.setImageResource(R.drawable.cloud2)
                "03n" -> binding.showimageView.setImageResource(R.drawable.cloud_sun2)
                "04n" -> binding.showimageView.setImageResource(R.drawable.cloud2)
                "09n" -> binding.showimageView.setImageResource(R.drawable.cloud_lighting)
                "10n" -> binding.showimageView.setImageResource(R.drawable.stormy)
                "11n" -> binding.showimageView.setImageResource(R.drawable.stormy)
                "13n" -> binding.showimageView.setImageResource(R.drawable.rain)
                "50n" -> binding.showimageView.setImageResource(R.drawable.rain)

            }

            hoursList = weather?.hourly ?: emptyList()
            dailyList = weather?.daily ?: emptyList()

            binding.pressurstext.text = weather.daily?.get(0)?.pressure.toString() + " hpa"
            binding.humiditytext.text = weather.daily?.get(0)?.humidity.toString() + " %"
            binding.windtext.text = weather.daily?.get(0)?.wind_speed.toString() + " m/s"
            binding.cloudtext.text = weather.daily?.get(0)?.clouds.toString() + " %"
            binding.ultraviolittext.text = weather.current?.uvi.toString() + ""
            binding.visibilitytext.text = weather.current?.visibility.toString() + " m"


            gethoursRecyleview()
            getDailyRecyleview()
        })

        viewModel.errorMessage.observe(requireActivity(), {
           // Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        viewModel.loading.observe(requireActivity(), Observer {
            if (it) {
                   } else {

            }
        })

        getCurrentLocation()
        viewModel.getAllMovies(lattitude, longtude, language, unites, "minutely", "fccb113f3db977a207025c87caa649c0")

        return binding.root
    }

    private fun gethoursRecyleview() {
        hoursRecycleView = binding.hoursRecycleView
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        hoursRecycleViewAdapter = HoursRecycleAdapter(hoursList, requireContext())
        hoursRecycleView.adapter = hoursRecycleViewAdapter
        hoursRecycleView.layoutManager = layoutManager
    }

    private fun getDailyRecyleview() {
        dailyRecycleView = binding.dailyRecycleView
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        dailyRecycleViewAdapter = DailyRecycleAdapter(dailyList, requireContext())
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
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()

            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()

           }
        }
    }

    @SuppressLint("ServiceCast")
    private fun isLocationIsEnabled(): Boolean {
        val locationManager: LocationManager =
            getActivity()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

                viewModel.getAllMovies(
                    lattitude,
                    longtude,
                    language,
                    unites,
                    "minutely",
                    "fccb113f3db977a207025c87caa649c0"
                )
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
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
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

            Log.i("TAG", "onLocationResult: ${lattitude} hhh ${longtude}")
            viewModel.getAllMovies(
                lattitude,
                longtude,
                language,
                unites,
                "minutely",
                "fccb113f3db977a207025c87caa649c0"
            )
        }
    }

}