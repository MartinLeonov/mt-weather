package com.martin.mt_weather

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.martin.mt_weather.databinding.ActivityMainBinding
import com.martin.mt_weather.tabs.TabAdapter
import com.martin.mt_weather.tabs.forecast.TabForecast
import com.martin.mt_weather.tabs.TabToday
import com.martin.mt_weather.weather.UserPreferencesRepository
import com.martin.mt_weather.weather.WeatherViewModel
import com.martin.mt_weather.weather.WeatherViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class MyTab(var name: String, var fragment: Fragment)

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel

    private var pages = arrayOf(
        MyTab("Today", TabToday()),
        MyTab("10 days", TabForecast()),
    )

    private fun refreshWeather() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Refreshing weather...",
            Snackbar.LENGTH_LONG
        ).show()
        viewModel.refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.pager.adapter = TabAdapter(this, this.pages)
        setSupportActionBar(binding.toolbar)

        TabLayoutMediator(binding.mainTabs, binding.pager) { tab, position ->
            tab.text = this.pages[position].name
        }.attach()

        viewModel = ViewModelProvider(
            this,
            WeatherViewModelFactory(UserPreferencesRepository.getInstance(this))
        ).get(
            WeatherViewModel::class.java
        )

        lifecycleScope.launch {
            viewModel.getCity().collect { city ->
                supportActionBar?.title = "${getString(R.string.app_name)} - $city"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Enter city"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query != "") {
                    lifecycleScope.launch {
                        viewModel.setCity(query)
                        viewModel.getCity().collect { city ->
                            supportActionBar?.title = "${getString(R.string.app_name)} - $city"
                            refreshWeather()
                            searchItem.collapseActionView()
                        }
                    }
                }
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_refresh -> {
                refreshWeather()
            }
        }
        return true
    }
}