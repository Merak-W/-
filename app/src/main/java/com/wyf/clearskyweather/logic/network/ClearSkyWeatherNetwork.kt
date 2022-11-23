package com.wyf.clearskyweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//统一网络数据源访问入口

object ClearSkyWeatherNetwork {
    //用ServiceCreator创建一个PlaceService/WeatherService接口的动态代理对象
    private val placeService = ServiceCreator.create<PlaceService>()
    private val weatherService = ServiceCreator.create(WeatherService::class.java)

    //定义函数，调起前面的placeService接口中的searchPlace()方法，发起搜索城市数据请求
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()
    suspend fun getDailyWeather(lng: String, lat: String) =
        weatherService.getDailyWeather(lng, lat).await()
    suspend fun getRealtimeWeather(lng: String, lat: String) =
        weatherService.getRealtimeWeather(lng, lat).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null){
                        continuation.resume(body)
                    } else {
                        continuation.resumeWithException(RuntimeException("response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}