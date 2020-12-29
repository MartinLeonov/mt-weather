package com.martin.mt_weather.tabs.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.martin.mt_weather.R
import com.martin.mt_weather.databinding.TabForecastBinding


class TabForecast : Fragment() {

    private lateinit var binding: TabForecastBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.tab_forecast, container, false)
        return binding.root
    }
}
