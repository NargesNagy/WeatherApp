package com.example.weatherapp.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.home.model.Hourly
import java.text.SimpleDateFormat
import java.util.*

class HoursRecycleAdapter  (val hours: List<Hourly>, val context : Context ) : RecyclerView.Adapter<HoursRecycleAdapter.ViewHolder>() {



    class ViewHolder(val itemView: View):RecyclerView.ViewHolder(itemView){

        val image : ImageView
            get() = itemView.findViewById(R.id.hour_image)
        val hourText : TextView
            get() = itemView.findViewById(R.id.hour_text)
        val temp : TextView = itemView.findViewById(R.id.temperature_hourse_text)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       // HoursCustomRowBinding.inflate(LayoutInflater.from(parent.context ), parent , false )
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hours_custom_row , parent , false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       // val hour = hours[position]
        //holder.image.setImageResource().toLong()).toString()
        holder.hourText.text =getDateTime(hours.get(position).dt)
        holder.temp.text = hours.get(position).temp.toString()

    }


        private fun getDateTime(s: Long?): String? {
            try {
                val sdf = SimpleDateFormat("hh:00 aaa")
                val netDate = Date(s!! * 1000)
                return sdf.format(netDate)
            } catch (e: Exception) {
                return e.toString()
            }

        }

   // fun setMyItems(myItems: ArrayList<Hourly>) { hours = myItems }


    override fun getItemCount(): Int {
       return hours.size
    }
}