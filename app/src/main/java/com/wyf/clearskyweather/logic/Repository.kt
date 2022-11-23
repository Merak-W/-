package com.wyf.clearskyweather.logic

import androidx.lifecycle.liveData
import com.wyf.clearskyweather.logic.dao.PlaceDao
import com.wyf.clearskyweather.logic.model.Place
import com.wyf.clearskyweather.logic.model.Weather
import com.wyf.clearskyweather.logic.network.ClearSkyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = ClearSkyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    //分别在两个async函数中发起网络请求，再分别调用await()方法
    //这样可以保证只有在两个网络请求都成功响应之后才进一步执行程序
    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        //由于async函数必须在协程作用域内才能调用
        //所以使用coroutineScope函数创建一个协程作用域
        coroutineScope {
            val deferredRealtime = async {
                ClearSkyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                ClearSkyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(
                    realtimeResponse.result.realtime,
                    dailyResponse.result.daily
                )
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    //fire()：按照liveData()函数的参数接收标准定义的一个高阶函数
    //在fire()函数内部调用一下liveData()函数
    //然后在liveData()函数的代码块中统一进行try catch处理，并在try语句中调用传入的Lambda表达式中的代码
    //最终获取Lambda表达式的执行结果并调用emit()方法发射出去
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavePlace() = PlaceDao.getSavePlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}