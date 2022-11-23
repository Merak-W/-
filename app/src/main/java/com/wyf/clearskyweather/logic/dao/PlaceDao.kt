package com.wyf.clearskyweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.wyf.clearskyweather.ClearSkyWeatherApplication
import com.wyf.clearskyweather.logic.model.Place

object PlaceDao {
    fun savePlace(place: Place) {
        sharedPreferences().edit() {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavePlace() : Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() =
        ClearSkyWeatherApplication.context.getSharedPreferences("clear_sky_weather", Context.MODE_PRIVATE)
}