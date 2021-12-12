package com.bqmz001.watchboard

import android.app.Application
import com.orhanobut.hawk.Hawk
import com.orhanobut.hawk.HawkBuilder
import com.pixplicity.sharp.Sharp
import com.qweather.sdk.view.HeConfig
import org.litepal.LitePal

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        HeConfig.switchToDevService()
        LitePal.initialize(this);
        Hawk.init(this)
            .build()

        if (Hawk.get("init",false)){
            initQWeather()
        }
    }
    fun initQWeather(){
        HeConfig.init(Hawk.get("qweather_public_id"), Hawk.get("qweather_api_key"))
    }
}