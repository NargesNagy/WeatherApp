package com.example.weatherapp

//import com.example.weatherapp.databinding.ActivityMainBinding

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class MainActivity : AppCompatActivity() {
   // lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
  //      setContentView(binding.root)

       val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
       val language = sharedPreferences.getString("languages", "en")
       Log.i("TAG", "getSettings: ${language}  vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv")

           val configuration: Configuration = resources.configuration
           var locale : Locale = Locale(language)
           Locale.setDefault(locale)
           if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
               configuration.setLocale(locale)

           }else{
               configuration.locale = locale
           }
           if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
               createConfigurationContext(configuration);
               //createConflgurationContext(configuration)

               resources.updateConfiguration(configuration , resources.displayMetrics)
               setContentView(R.layout.activity_main)

           }else{
               setContentView(R.layout.activity_main)
           }




        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = findNavController(R.id.fragmentContainerView)
        bottomNavigation.setupWithNavController(navController)


    }


    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}