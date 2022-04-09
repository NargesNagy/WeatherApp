package com.example.weatherapp.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.home.model.Daily
import com.example.weatherapp.home.model.Hourly
import java.text.SimpleDateFormat
import java.util.*

class DailyRecycleAdapter (val daily: List<Daily>, val context : Context) : RecyclerView.Adapter<DailyRecycleAdapter.ViewHolder>() {



    class ViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView){

        val dailyimage : ImageView
            get() = itemView.findViewById(R.id.daily_image)
        val dayNameText : TextView = itemView.findViewById(R.id.dayName)
        val daydescription : TextView = itemView.findViewById(R.id.dailyDescription)
        val dailytemp : TextView = itemView.findViewById(R.id.dailyTemperature)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // HoursCustomRowBinding.inflate(LayoutInflater.from(parent.context ), parent , false )
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_custom_row , parent , false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // val hour = hours[position]
        //holder.dailyimage.setImageResource().toLong()).toString()
        holder.dayNameText.text =getDateTime(daily.get(position).dt)
        holder.daydescription.text = daily.get(position).weather?.get(0).description
        holder.dailytemp.text =daily.get(position).temp.max.toString()+"/"+daily.get(position).temp.min
        var icon = daily.get(position).weather?.get(0).icon

        when (icon){
            "01d" -> holder.dailyimage.setImageResource(R.drawable.suny)
            "02d" -> holder.dailyimage.setImageResource(R.drawable.cloud_sun2)
            "03d" -> holder.dailyimage.setImageResource(R.drawable.cloud2)//blackcloud_lighting
            "04d" -> holder.dailyimage.setImageResource(R.drawable.cloud2)
            "09d" -> holder.dailyimage.setImageResource(R.drawable.rain)
            "10d" -> holder.dailyimage.setImageResource(R.drawable.clouds__rain_sun)
            "11d" -> holder.dailyimage.setImageResource(R.drawable.stormy)
            "13d" -> holder.dailyimage.setImageResource(R.drawable.snow)
            "50d" -> holder.dailyimage.setImageResource(R.drawable.darkcloud_rain)
            "01n" -> holder.dailyimage.setImageResource(R.drawable.suny)
            "02n" -> holder.dailyimage.setImageResource(R.drawable.cloud_sun2)
            "03n" -> holder.dailyimage.setImageResource(R.drawable.cloud2)
            "04n" -> holder.dailyimage.setImageResource(R.drawable.cloud2)
            "09n" -> holder.dailyimage.setImageResource(R.drawable.rain)
            "10n" -> holder.dailyimage.setImageResource(R.drawable.clouds__rain_sun)
            "11n" -> holder.dailyimage.setImageResource(R.drawable.stormy)
            "13n" -> holder.dailyimage.setImageResource(R.drawable.snow)
            "50n" -> holder.dailyimage.setImageResource(R.drawable.darkcloud_rain)

        }
    }


    private fun getDateTime(s: Long?): String? {
        try {
            val sdf = SimpleDateFormat("EEEE")
            val netDate = Date(s!! * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }

    }

    // fun setMyItems(myItems: ArrayList<Hourly>) { hours = myItems }

    override fun getItemCount(): Int {
        return daily.size
    }


}