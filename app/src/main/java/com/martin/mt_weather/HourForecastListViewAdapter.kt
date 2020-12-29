package com.martin.mt_weather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.martin.mt_weather.weather.json.Hour
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HourForecastListViewAdapter(private val context: Context, hours: List<Hour>, filterCurrentHour: Boolean) :
    BaseAdapter() {
    private var list = ArrayList<Hour>()

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    init {
        val now = Date()
        if (filterCurrentHour) {
            this.list = hours.filter { hour ->
                val parser = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val date = parser.parse(hour.time)
                date != null && !date.before(now)
            } as ArrayList<Hour>
        } else {
            list = hours as ArrayList<Hour>
        }

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
            view = inflater.inflate(R.layout.tab_hour_item, parent, false)
            holder = ViewHolder()
            holder.temp = view.findViewById(R.id.temp) as TextView
            holder.hour = view.findViewById(R.id.hour) as TextView
            holder.conditionIcon = view.findViewById(R.id.conditionIcon) as ImageView
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val hour = getItem(position) as Hour
        "${hour.temp_c}°C".also { holder.temp.text = it }

        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val p = parser.parse(hour.time)
        if (p != null) {
            val date = SimpleDateFormat("HH:mm", Locale.getDefault()).format(p)
            date.also { holder.hour.text = it }
        }

        Picasso.with(context).load(hour.condition.icon).centerCrop().resize(128, 128).into(
                holder.conditionIcon
            )

        return view
    }

    private class ViewHolder {
        lateinit var conditionIcon: ImageView
        lateinit var hour: TextView
        lateinit var temp: TextView
    }

}