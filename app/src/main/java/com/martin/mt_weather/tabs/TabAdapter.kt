package com.martin.mt_weather.tabs

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.martin.mt_weather.MyTab

class TabAdapter(activity: AppCompatActivity, val items: Array<MyTab>) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return items[position].fragment
    }
}
