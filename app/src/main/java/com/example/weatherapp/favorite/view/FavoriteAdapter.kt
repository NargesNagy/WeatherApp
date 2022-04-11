package com.example.weatherapp.favorite.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FavoriteCustomRowBinding
import com.example.weatherapp.favorite.model.FavoriteModel
import com.example.weatherapp.home.model.Hourly
import com.example.weatherapp.home.view.HoursRecycleAdapter

class FavoriteAdapter (val favoteDeleteClickInterface: FavoriteOnDeleteClickInterface, val favoriteClickInterface : FavoriteClickInterface) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {


    //val context : Context
    private var favorites = ArrayList<FavoriteModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavoriteCustomRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding) }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.favoritecitytext.text = favorites.get(position).areaname
        holder.binding.deleteitybtn.setOnClickListener {
            favoteDeleteClickInterface.onDeleteClick(favorites.get(position))

        }
        holder.binding.favoritecustomrow.setOnClickListener {
            favoriteClickInterface.onFavoriteClick(favorites.get(position))

        }

    }

    override fun getItemCount(): Int {
       return favorites.size
    }

    fun setList(favorit: ArrayList<FavoriteModel>){
        favorites.clear()
        favorites.addAll(favorit)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding : FavoriteCustomRowBinding) : RecyclerView.ViewHolder(binding.root) {}

}