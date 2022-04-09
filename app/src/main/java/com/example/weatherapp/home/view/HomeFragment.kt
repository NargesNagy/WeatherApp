package com.example.weatherapp.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.Repository
import com.example.weatherapp.data.remotesource.RetrofitService
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.home.viewmodel.MyViewModel
import com.example.weatherapp.home.model.Daily
import com.example.weatherapp.home.model.Hourly
import com.example.weatherapp.home.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    lateinit var viewModel: MyViewModel
    lateinit var binding : FragmentHomeBinding

    lateinit var hoursRecycleView : RecyclerView;
    lateinit var hoursRecycleViewAdapter :HoursRecycleAdapter ;
    lateinit var hoursList : List<Hourly>

    lateinit var dailyRecycleView : RecyclerView;
    lateinit var dailyRecycleViewAdapter :DailyRecycleAdapter ;
    lateinit var dailyList : List<Daily>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(context) , container , false)//,container , false)
        //setContentView(binding.root)
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

            //  adapter.setMovies(it)

            val weather = it
            val timezone = weather.timezone

            //Log.i("TAG", "onViewCreated: " + timezone)
            binding.countryNameHomeText.text = timezone
            binding.temperatureHomeText.text = weather.current.temp.toInt().toString()
            Log.i("TAG", "onCreateView: ${weather.current.weather?.get(0)?.icon}")
           // binding.imageView.setImageResource()

            val formatedDate: String = SimpleDateFormat("EEE, d MMM yyyy ", Locale.ENGLISH).format(Date())
            binding.dateHomeText.text = formatedDate

            hoursList = weather?.hourly ?: emptyList()
            dailyList = weather?.daily?: emptyList()

            binding.pressurstext.text = weather.daily?.get(0)?.pressure.toString() + " hpa"
            binding.humiditytext.text=weather.daily?.get(0)?.humidity.toString() + " %"
            binding.windtext.text=weather.daily?.get(0)?.wind_speed.toString() + " m/s"
            binding.cloudtext.text=weather.daily?.get(0)?.clouds.toString() + " %"
            binding.ultraviolittext.text=weather.current?.uvi.toString() + ""
            binding.visibilitytext.text=weather.current?.visibility.toString() + " m"

            gethoursRecyleview()
            getDailyRecyleview()
        })

        viewModel.errorMessage.observe(requireActivity(), {
            //Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.loading.observe(requireActivity(), Observer {
            if (it) {
                //  binding.progressDialog.visibility = View.VISIBLE
            } else {
                // binding.progressDialog.visibility = View.GONE
            }
        })

        viewModel.getAllMovies(33.44 , -94.04 ,"en" , "metric")//

        return binding.root
    }

    private fun gethoursRecyleview() {
        hoursRecycleView = binding.hoursRecycleView
        val layoutManager = LinearLayoutManager(requireContext() )
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        hoursRecycleViewAdapter = HoursRecycleAdapter( hoursList , requireContext())
        hoursRecycleView.adapter = hoursRecycleViewAdapter
        hoursRecycleView.layoutManager = layoutManager
    }

    private fun getDailyRecyleview() {
        dailyRecycleView = binding.dailyRecycleView
        val layoutManager = LinearLayoutManager(requireContext() )
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        dailyRecycleViewAdapter = DailyRecycleAdapter(dailyList , requireContext())
        dailyRecycleView.adapter = dailyRecycleViewAdapter
        dailyRecycleView.layoutManager = layoutManager
    }

    companion object {

    }
}