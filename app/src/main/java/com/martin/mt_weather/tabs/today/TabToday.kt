package com.martin.mt_weather.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.martin.mt_weather.HourForecastListViewAdapter
import com.martin.mt_weather.R
import com.martin.mt_weather.databinding.TabTodayBinding
import com.martin.mt_weather.weather.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*


class TabToday : Fragment(R.layout.tab_today) {

    private lateinit var binding: TabTodayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.tab_today, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
        model.response.observe(viewLifecycleOwner, Observer {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val now = sdf.format(Date())

            val today = it.forecast.forecastday.find { f -> f.date == now }
            val date = SimpleDateFormat("EEEE d. M.", Locale.getDefault()).format(Date())
            binding.dateText.text = date

            if (today != null) {
                binding.listView.adapter = this.context?.let { it1 ->
                    HourForecastListViewAdapter(
                        it1,
                        today.hour,
                        true
                    )
                }
            }
        })
    }
}

