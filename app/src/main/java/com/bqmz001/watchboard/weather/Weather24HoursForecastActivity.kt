package com.bqmz001.watchboard.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bqmz001.watchboard.databinding.ActivityWeather24HoursForecastBinding
import com.orhanobut.hawk.Hawk
import com.qweather.sdk.bean.base.Code
import com.qweather.sdk.bean.weather.WeatherHourlyBean
import com.qweather.sdk.view.QWeather
import org.joda.time.DateTime

class Weather24HoursForecastActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeather24HoursForecastBinding
    lateinit var adapter: Weather24HoursForecastAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeather24HoursForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener { finish() }

        binding.tvPosition.setText("${Hawk.get("qweather-city-simple","")} 未来24小时预报")


        binding.rvList.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adapter = Weather24HoursForecastAdapter(mutableListOf())
        binding.rvList.adapter = adapter

        val weatherHourlyBean = Hawk.get<WeatherHourlyBean>("forecast-24h")
        val updateTime = Hawk.get<Long>("forcast-24h-time")
        val interval = Hawk.get<Int>("refresh")
        if (interval == null || interval == 0) {
            Hawk.put("refresh", 600000)
        }
        if (weatherHourlyBean == null || updateTime == null || DateTime.now().millis - updateTime > interval) {
            get24HoursForecast()
        } else {
            adapter.setList(weatherHourlyBean.hourly)
        }

    }

    fun get24HoursForecast() {
        QWeather.getWeather24Hourly(this, Hawk.get("qweather-city-id",""), object : QWeather.OnResultWeatherHourlyListener {
                override fun onError(p0: Throwable?) {
                    p0!!.printStackTrace()
                    Toast.makeText(
                        this@Weather24HoursForecastActivity,
                        "数据错误或数据过期,请检查API ID和KEY是否配置正确或检查网络配置",
                        Toast.LENGTH_LONG
                    )
                        .show()

                }

                override fun onSuccess(p0: WeatherHourlyBean?) {
                    if (p0!!.code == Code.OK) {
                        Hawk.put("forecast-24h", p0)
                        Hawk.put("forcast-24h-time", DateTime.now().millis)
                        adapter.setList(p0.hourly)
                    }
                }
            })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == 219) {
            finish()
        }
        return true
    }
}