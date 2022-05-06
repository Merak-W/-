package com.wyf.clearskyweather.logic.network

import com.wyf.clearskyweather.ClearSkyWeatherApplication
import com.wyf.clearskyweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/*
    当调用searchPlaces()方法时，Retrofit就会自动发起一条GET请求，去访问@GET注解中配置的地址
    搜索城市API中query需要动态指定，用@Query注解实现
    return：Call<PlaceResponse>
    Retrofit会将服务器返回的JSON数据自动解析成PlaceResponse对象
 */

interface PlaceService {
    @GET("v2/place?token=${ClearSkyWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}