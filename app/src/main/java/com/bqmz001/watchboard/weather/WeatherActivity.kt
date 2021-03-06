package com.bqmz001.watchboard.weather

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.bqmz001.watchboard.R
import com.bqmz001.watchboard.databinding.ActivityWeatherBinding
import com.orhanobut.hawk.Hawk
import com.pixplicity.sharp.OnSvgElementListener
import com.pixplicity.sharp.Sharp
import com.qweather.sdk.bean.weather.WeatherNowBean
import org.joda.time.DateTime

class WeatherActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeatherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val nowTime = DateTime.now()

        binding.tvPosition.setText(Hawk.get("qweather-city", ""))

        binding.ivBack.setOnClickListener { finish() }
        binding.tv24hourWeather.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Weather24HoursForecastActivity::class.java
                )
            )
        }
        binding.tv7dayWeather.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Weather7DaysForecastActivity::class.java
                )
            )
        }

        val nowWeather = Hawk.get<WeatherNowBean>("now-weather")
        val nowWeatherTime = Hawk.get<Long>("now-weather-time")
        val advancedEndpoint = Hawk.get<Boolean>("advanced", false)

        if (!advancedEndpoint) {
            binding.cvMore.visibility = View.INVISIBLE
        } else {
            binding.cvMore.visibility = View.VISIBLE
        }

        val interval = Hawk.get<Int>("refresh")
        if (interval == null || interval == 0) {
            Hawk.put("refresh", 600000)
        }
        if (nowWeather != null && (nowTime.millis - nowWeatherTime < interval)) {
            binding.tvTempFeel.setText("${nowWeather.now.feelsLike}??C")
            binding.tvTempActual.setText("${nowWeather.now.temp}??C")
            binding.tvWind.setText("${nowWeather.now.windScale}???\n${nowWeather.now.windDir}")
            binding.tvWet.setText("${nowWeather.now.humidity}\n%")
            binding.tvRain.setText("${nowWeather.now.precip}\nmm")
            binding.tvPressure.setText("${nowWeather.now.pressure}\nhPa")
            binding.tvVis.setText("${nowWeather.now.vis}\nkm")
            binding.tvUpdateTime.setText("????????????:${DateTime(nowWeatherTime).toString("yyyy-MM-dd HH:mm")}")
            val sharp = Sharp.loadAsset(assets, "weather_icons/${nowWeather.now.icon}.svg")
            sharp.setOnElementListener(object : OnSvgElementListener {
                override fun onSvgStart(p0: Canvas, p1: RectF?) {
                }

                override fun onSvgEnd(p0: Canvas, p1: RectF?) {
                }

                override fun <T : Any?> onSvgElement(
                    p0: String?,
                    p1: T,
                    p2: RectF?,
                    p3: Canvas,
                    p4: RectF?,
                    p5: Paint?
                ): T {
                    if (Hawk.get("dark", false) == false) {
                        p5!!.color = resources.getColor(R.color.grey900)
                    } else {
                        p5!!.color = resources.getColor(R.color.grey300)
                    }
                    return p1
                }

                override fun <T : Any?> onSvgElementDrawn(
                    p0: String?,
                    p1: T,
                    p2: Canvas,
                    p3: Paint?
                ) {
                }

            })
            sharp.into(binding.ivWeatherIcon)

        } else {
            Toast.makeText(this, "???????????????????????????,?????????API ID???KEY???????????????????????????????????????", Toast.LENGTH_LONG).show()
            binding.hsvContainer.visibility = View.GONE

        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == 219) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)

    }

}