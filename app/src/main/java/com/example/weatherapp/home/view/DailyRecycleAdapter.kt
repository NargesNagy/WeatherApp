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