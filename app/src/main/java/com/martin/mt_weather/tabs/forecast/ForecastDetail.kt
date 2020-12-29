package com.martin.mt_weather.tabs.forecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.martin.mt_weather.databinding.WeatherDetailBinding
import com.martin.mt_weather.HourForecastListViewAdapter
import com.martin.mt_weather.R
import com.martin.mt_weather.weather.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "date"


class ForecastDetail : Fragment() {
    private var selectedDate: String? = null
    private lateinit var binding: WeatherDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedDate = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.weather_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val p = parser.parse(selectedDate)
        if (p != null) {
            val date = SimpleDateFormat("EEEE d. M.", Locale.getDefault()).format(p)
            date.also { binding.dateText.text = it }
        }

        binding.btnWeatherDetailBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val model = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
        model.response.observe(viewLifecycleOwner, Observer {
            if (selectedDate != null) {
                val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selectedDate)
                if (parsedDate != null) {
                    val now = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate)
                    val date = it.forecast.forecastday.find { f -> f.date == now }
                    if (date != null) {
                        binding.listView.adapter = this.context?.let { it1 ->
                            HourForecastListViewAdapter(
                                it1,
                                date.hour,
                                false
                            )
                        }
                    }
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ForecastDetail().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}