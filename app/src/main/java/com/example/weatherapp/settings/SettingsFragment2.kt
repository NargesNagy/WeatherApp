package com.example.weatherapp.settings

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeBinding
import java.util.*

class SettingsFragment2 : PreferenceFragmentCompat() {
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val language = sharedPreferences.getString("languages", "").toString()


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
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

        } else {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }


    }

}

