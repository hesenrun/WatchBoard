package com.bqmz001.watchboard.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bqmz001.watchboard.databinding.ActivityWeather7DaysForecastBinding
import com.orhanobut.hawk.Hawk
import com.qweather.sdk.bean.base.Code
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.view.QWeather
import org.joda.time.DateTime

class Weather7DaysForecastActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeather7DaysForecastBinding
    lateinit var adapter: Weather7DaysForecastAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeather7DaysForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener { finish() }

        binding.tvPosition.setText("${Hawk.get("qweather-city-simple", "")} 7天预报")

        binding.rvList.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adapter = Weather7DaysForecastAdapter(mutableListOf())
        binding.rvList.adapter = adapter

        val weatherDaliyBean = Hawk.get<WeatherDailyBean>("forecast-7d")
        val updateTime = Hawk.get<Long>("forcast-7d-time")
        val interval = Hawk.get<Int>("refresh")
        if (interval == null || interval == 0) {
            Hawk.put("refresh", 600000)
        }

        if (weatherDaliyBean == null || updateTime == null || DateTime.now().millis - updateTime > interval) {
            get7DaysForecast()
        } else {
            adapter.setList(weatherDaliyBean.daily)
        }
    }

    fun get7DaysForecast() {
        QWeather.getWeather7D(this,
            Hawk.get("qweather-city-id", ""), object : QWeather.OnResultWeatherDailyListener {
                override fun onError(p0: Throwable?) {
                    p0!!.printStackTrace()
                    Toast.makeText(
                        this@Weather7DaysForecastActivity,
                        "数据错误或数据过期,请检查API ID和KEY是否配置正确或检查网络配置",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onSuccess(p0: WeatherDailyBean?) {
                    if (p0!!.code == Code.OK) {
                        Hawk.put("forecast-7d", p0)
                        Hawk.put("forcast-7d-time", DateTime.now().millis)
                        adapter.setList(p0.daily)
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