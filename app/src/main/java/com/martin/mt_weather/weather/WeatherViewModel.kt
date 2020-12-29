package com.martin.mt_weather.weather

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.*
import com.martin.mt_weather.weather.json.Weather
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserPreferencesRepository private constructor(context: Context) {
    private val defaultCity = "Brno"
    private val dataStore: DataStore<Preferences> =
        context.createDataStore(name = "weather")

    private object PreferencesKeys {
        val CITY = preferencesKey<String>("city")
        val WEATHER = preferencesKey<String>("weather")
    }

    suspend fun updateWeather(weather: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.WEATHER] = weather
        }
    }

    fun getWeather(): Flow<String?> {
        return dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.WEATHER]
            }
    }

    suspend fun updateCity(city: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CITY] = city
        }
    }

    fun getCity(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.CITY] ?: defaultCity
            }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferencesRepository? = null

        fun getInstance(context: Context): UserPreferencesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = UserPreferencesRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }
}

class WeatherViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _response = MutableLiveData<Weather>()

    val response: LiveData<Weather>
        get() = _response

    init {
        getWeather()
    }

    suspend fun setCity(city: String) {
        userPreferencesRepository.updateCity(city)
    }

    fun getCity(): Flow<String> {
        return userPreferencesRepository.getCity()
    }

    private suspend fun updateWeather(weather: String) {
        userPreferencesRepository.updateWeather(weather)
    }

    private fun getWeather() {
        viewModelScope.launch {
            val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter: JsonAdapter<Weather> = moshi.adapter(Weather::class.java)

            userPreferencesRepository.getWeather().collect { weather ->
                if (weather != null) {
                    val w = adapter.fromJson(weather)
                    _response.value = w
                }
            }

            try {
                userPreferencesRepository.getCity().collectLatest { value ->
                    val w = WeatherAPI.retrofitService.getWeather(value, "10")

                    updateWeather(adapter.toJson(w))
                    _response.value = w
                }
            } catch (e: Exception) {
                e.message?.let { Log.e("error", it) }
            }
        }
    }

    fun refresh() {
        getWeather()
    }
}

class WeatherViewModelFactory(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}