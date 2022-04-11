package com.example.weatherapp.favorite.view

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Application
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFavoriteBinding
import com.example.weatherapp.favorite.model.FavoriteModel
import com.example.weatherapp.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapp.home.view.DailyRecycleAdapter
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.fixedRateTimer


class FavoriteFragment : Fragment(), FavoriteClickInterface, FavoriteOnDeleteClickInterface {

    lateinit var binding: FragmentFavoriteBinding
    private val AUTOCOMPLETE_REQUEST_CODE = 100
    lateinit var viewModal: FavoriteViewModel
    private lateinit var favAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoriteBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )//,container , false)
        binding.savebtn.visibility = View.GONE
        binding.editcitynametext.visibility = View.GONE
        binding.favoriteFloatingbtn.visibility = View.VISIBLE

        favAdapter = FavoriteAdapter(this, this)

        Places.initialize(requireContext(), "AIzaSyDPGssUuT4_lvpJ4nNDn2ea_MWY1XQ132w")

        // initializes the viewmodel.
        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(FavoriteViewModel::class.java)
        getDailyRecyleview()
        observeg()

         // auto complete places
        /*
        binding.favoriteFloatingbtn.setOnClickListener {

            Log.i("TAG", "onActivityResult:vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv ")

            val fields = listOf(Place.Field.ID, Place.Field.NAME)

            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(requireActivity())

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)

        }
*/

        binding.favoriteFloatingbtn.setOnClickListener {
            binding.savebtn.visibility = View.VISIBLE
            binding.editcitynametext.visibility = View.VISIBLE
            binding.favoriteRecycle.visibility = View.GONE



            binding.savebtn.setOnClickListener {
                binding.favoriteFloatingbtn.visibility = View.GONE
                Log.i("TAG", "onCreateView: lkjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj")
                var city = binding.editcitynametext.text.toString()
                var gc = Geocoder(requireActivity(), Locale.getDefault())
                var addresses = gc.getFromLocationName(city, 1)
                if (!addresses.isNullOrEmpty()) {
                    var address = addresses.get(0)
                    binding.editcitynametext.visibility = View.GONE
                    binding.savebtn.visibility = View.GONE
                    binding.favoriteFloatingbtn.visibility = View.VISIBLE
                    val favorite = FavoriteModel(
                        address.hashCode(),
                        binding.editcitynametext.text.toString(),
                        address.latitude,
                        address.longitude
                    )
                    viewModal.insertCity(favorite)
                  //  Log.i("TAG", "onCreateView: adedddddddddddddddddddddddddddddddddddddddddddddddd")

                    //binding.latlongtext.setText("latt ${address.latitude} ${address.longitude} ${address.adminArea}")
                    binding.favoriteRecycle.visibility = View.VISIBLE
                    binding.favoriteFloatingbtn.visibility = View.VISIBLE
                } else {
                    Toast.makeText(requireContext(), "Plesse enter valid area", Toast.LENGTH_SHORT).show()
                  //  Log.i("TAG", "onCreateView: lkjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj")

                }
            }
        }

        return binding.root
    }

    private fun getDailyRecyleview() {
        binding.favoriteRecycle.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favAdapter
        }
    }

    public fun observeg() {
        viewModal.favorites.observe(requireActivity(), androidx.lifecycle.Observer {
            favAdapter.setList(it as ArrayList<FavoriteModel>)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("TAG", "onActivityResult:sssssssssssssssssssssssssssssssssssssssss ")

        //super.onActivityResult(requestCode, resultCode, data)
        Log.i("TAG", "${resultCode}:aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa ")

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            Log.i("TAG", "onActivityResult:eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee ")
/*
            if(resultCode == 2){
                val intent: Intent? = data
                val place = Autocomplete.getPlaceFromIntent(intent)
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());

            }else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val intent: Intent? = data

                val status = Autocomplete.getStatusFromIntent(data)
                Log.i("TAG", status.statusMessage ?: "jjjjjjjjjjjjjjjjjjjjjj")
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;

*/
            when (resultCode) {

                2 -> { // Activity.RESULT_OK
                    Log.i(
                        "TAG",
                        "onActivityResult:kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk "
                    )

                    data?.let {
                        Log.i("TAG", "onActivityResult:bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb ")

                        val place = Autocomplete.getPlaceFromIntent(data)
                        binding.latlongtext.text = place.latLng.toString()
                        Log.i(
                            "TAG",
                            "Place: ${place.name} , ${place.address} ,${place.latLng} , ${place.id}"
                        )
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i("TAG", status.statusMessage ?: "jjjjjjjjjjjjjjjjjjjjjj")
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                    Log.i("TAG", "canceleddddddddddddddddddddddddddddd")

                }
            }

            return

        }

    }

    companion object {

    }

    override fun onFavoriteClick(model: FavoriteModel) {


        var bundle = Bundle()
        model.latitude?.let { bundle.putDouble("latitude", it) }
        model.longtude?.let { bundle.putDouble("longtude", it) }

        val df = FavoriteDetailsFragment()
        df.arguments = bundle
        fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView, df)?.commit()
        // Navigation.findNavController(requireView()).navigate(R.id.action_favoriteFragment2_to_favoriteDetailsFragment)
    }

    override fun onDeleteClick(model: FavoriteModel) {
        binding.favoriteRecycle.clearFocus()
        viewModal.deleteCity(model)
    }


}