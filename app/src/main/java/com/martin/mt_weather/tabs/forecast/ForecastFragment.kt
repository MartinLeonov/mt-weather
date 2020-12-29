package com.martin.mt_weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.martin.mt_weather.databinding.FragmentForecastBinding
import com.martin.mt_weather.weather.WeatherViewModel
import com.martin.mt_weather.weather.json.Forecastday
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ForecastFragment : Fragment() {
    private lateinit var binding: FragmentForecastBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selectedItem = parent.getItemAtPosition(position) as Forecastday
                val args = Bundle()
                args.putSerializable("date", selectedItem.date)
                findNavController().navigate(R.id.action_forecastFragment_to_forecastDetail, args)
            }
        val model = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
        model.response.observe(viewLifecycleOwner, {
            binding.listView.adapter = this.context?.let { it1 ->
                ForecastListViewAdapter(
                    it1,
                    it.forecast.forecastday
                )
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForecastFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}

class ForecastListViewAdapter(private val context: Context, moviesList: List<Forecastday>) : BaseAdapter() {
    private var list = ArrayList<Forecastday>()

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    init {
        this.list = moviesList as ArrayList<Forecastday>
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.tab_forecast_item, parent, false)
            holder = ViewHolder()
            holder.dateTextView = view.findViewById(R.id.date) as TextView
            holder.conditionTextView = view.findViewById(R.id.condition) as TextView
            holder.tempDayTextView = view.findViewById(R.id.temp_day) as TextView
            holder.tempNightTextView = view.findViewById(R.id.temp_night) as TextView
            holder.conditionIcon = view.findViewById(R.id.conditionIcon) as ImageView
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val forecast = getItem(position) as Forecastday

        val now = Date()
        val c = Calendar.getInstance()
        c.time = now
        c.add(Calendar.DATE, 1)


        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = sdf.format(Date())
        val tomorrow = sdf.format(c.time)
        when (forecast.date) {
            today -> {
                "Today".also { holder.dateTextView.text = it }
            }
            tomorrow -> {
                "Tomorrow".also { holder.dateTextView.text = it }
            }
            else -> {
                val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val p = parser.parse(forecast.date)
                if (p != null) {
                    val date = SimpleDateFormat("EEEE d. M.", Locale.getDefault()).format(p)
                    date.also { holder.dateTextView.text = it }
                }
            }
        }

        holder.conditionTextView.text = forecast.day.condition.text

        "${forecast.day.maxtemp_c}°C".also { holder.tempDayTextView.text = it }
        "${forecast.day.mintemp_c}°C".also { holder.tempNightTextView.text = it }

        Picasso.with(context).load(forecast.day.condition.icon).centerCrop().resize(128, 128).into(holder.conditionIcon)

        return view
    }

    private class ViewHolder {
        lateinit var conditionIcon: ImageView
        lateinit var dateTextView: TextView
        lateinit var conditionTextView: TextView
        lateinit var tempDayTextView: TextView
        lateinit var tempNightTextView: TextView
    }

}
