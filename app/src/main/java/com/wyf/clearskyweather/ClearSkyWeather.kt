package com.wyf.clearskyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class ClearSkyWeatherApplication : Application() {

    companion object {
        //由于此处获取的不是Activity或Service中的Context，而是Application中的
        // 所以全局只会存在一份实例，并且在整个应用程序的生命周期内都不会回收
        // 因此不会存在内存泄漏风险
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        const val TOKEN = "W6FjrzHH5nukfucd"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}