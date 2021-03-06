package com.bqmz001.watchboard

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import com.bqmz001.watchboard.alarm.AlarmActivity
import com.bqmz001.watchboard.calendar.CalendarActivity
import com.bqmz001.watchboard.databinding.ActivityMainBinding
import com.bqmz001.watchboard.settings.SettingsActivity
import com.bqmz001.watchboard.weather.WeatherActivity
import com.nlf.calendar.Lunar
import com.orhanobut.hawk.Hawk
import com.pixplicity.sharp.Sharp
import com.qweather.sdk.bean.base.Code
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.qweather.sdk.view.QWeather
import org.joda.time.DateTime
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import com.pixplicity.sharp.OnSvgElementListener


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var receiver: TimeReceiver
    var shortPress = false

    var time = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Hawk.get("dark", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cvTime.setOnClickListener {
            startActivity(Intent(this, AlarmActivity::class.java))
        }

        binding.cvDate.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }

        binding.cvWeather.setOnClickListener {
            startActivity(Intent(this, WeatherActivity::class.java))
        }

        binding.cvTime.setOnLongClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }

        binding.cvDate.setOnLongClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }

        binding.cvWeather.setOnLongClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }

        receiver = TimeReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_TICK)
        registerReceiver(receiver, filter)
        refreshTime()
        refreshAlarm(this)

        disableBatteryOptimization()

    }

    override fun onDestroy() {
        if (this::receiver.isInitialized)
            unregisterReceiver(receiver)
        super.onDestroy()
    }

    fun refreshTime() {
        val dateTime = DateTime.now()
        binding.tvHour.setText(String.format("%02d", dateTime.hourOfDay))
        binding.tvMinute.setText(String.format("%02d", dateTime.minuteOfHour))
        binding.tvDate.setText(
            "${dateTime.year}???${
                String.format(
                    "%02d",
                    dateTime.monthOfYear
                )
            }???${String.format("%02d", dateTime.dayOfMonth)}??? "
        )

        val lunar = Lunar()
        binding.tvWeekday.setText(
            "${getWeekDay(dateTime.dayOfWeek)} ${lunar.monthInChinese}???${lunar.dayInChinese} ${lunar.jieQi}"
        )
        queryWeather(dateTime)

    }

    fun queryWeather(dateTime: DateTime) {
        val interval = Hawk.get<Int>("refresh")
        if (interval == null || interval == 0) {
            Hawk.put("refresh", 600000)
        }
        if (dateTime.millis - time > interval) {
            time = dateTime.millis
            QWeather.getWeatherNow(
                this,
                Hawk.get("qweather-city-id", ""),
                object : QWeather.OnResultWeatherNowListener {
                    override fun onError(p0: Throwable?) {
                        p0!!.printStackTrace()
                        Toast.makeText(
                            this@MainActivity,
                            "????????????,?????????API ID???KEY???????????????????????????????????????",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                    override fun onSuccess(p0: WeatherNowBean?) {
                        if (p0!!.code == Code.OK) {
                            Hawk.put("now-weather", p0)
                            Hawk.put("now-weather-time", dateTime.millis)
                            binding.tvTemp.setText("${p0.now.temp}??C")
                            val sharp = Sharp.loadAsset(assets, "weather_icons/${p0.now.icon}.svg")
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
                        }
                    }
                })
        }
    }

    @SuppressLint("BatteryLife")
    fun disableBatteryOptimization() {
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WatchBoard:Lock")
        wl.acquire()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !pm.isIgnoringBatteryOptimizations(
                packageName
            )
        ) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:" + packageName)
            startActivity(intent)
        }
    }

    inner class TimeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent!!.action.equals(Intent.ACTION_TIME_TICK)) {
                refreshTime()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //????????????keycode 219
        if (keyCode == 219) {
            if (event!!.getAction() == KeyEvent.ACTION_DOWN) {
                event.startTracking();
                if (event.getRepeatCount() == 0) {
                    shortPress = true;
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == 219) {
            shortPress = false;
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            return true;
        }
        return false;
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == 219) {
            if (shortPress) {
                Toast.makeText(this, "??????????????????????????????", Toast.LENGTH_LONG).show();
            } else {

            }
            shortPress = false;
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

}

