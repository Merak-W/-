package com.wyf.clearskyweather.logic.network

import com.wyf.clearskyweather.ClearSkyWeatherApplication
import com.wyf.clearskyweather.logic.model.DailyResponse
import com.wyf.clearskyweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

//访问天气信息API的Retrofit接口
interface WeatherService {

    @GET("v2.5/${ClearSkyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String):
            Call<RealtimeResponse>
    @GET("v2.5/${ClearSkyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String):
            Call<DailyResponse>
}