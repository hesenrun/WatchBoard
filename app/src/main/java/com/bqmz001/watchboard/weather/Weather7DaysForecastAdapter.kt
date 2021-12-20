package com.bqmz001.watchboard.weather

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.widget.ImageView
import com.bqmz001.watchboard.R
import com.bqmz001.watchboard.getWeekDay
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.orhanobut.hawk.Hawk
import com.pixplicity.sharp.OnSvgElementListener
import com.pixplicity.sharp.Sharp
import com.qweather.sdk.bean.weather.WeatherDailyBean
import org.joda.time.format.DateTimeFormat

class Weather7DaysForecastAdapter(list: MutableList<WeatherDailyBean.DailyBean>) :
    BaseQuickAdapter<WeatherDailyBean.DailyBean, BaseViewHolder>(
        R.layout.item_7_day_forecast,
        list
    ) {
    override fun convert(holder: BaseViewHolder, item: WeatherDailyBean.DailyBean) {
        holder.setText(
            R.id.tv_time,
            "${item.fxDate.substring(5, 10)}\n${
                getWeekDay(
                    DateTimeFormat.forPattern("yyyy-MM-dd")
                        .parseDateTime(item.fxDate.substring(0, 10)).dayOfWeek
                )
            }"
        )
            .setText(R.id.tv_temp_day, item.tempMax + "°C")
            .setText(R.id.tv_temp_night, item.tempMin + "°C")

        val imageDay = holder.getView<ImageView>(R.id.iv_weather_icon_day)
        val imagenight = holder.getView<ImageView>(R.id.iv_weather_icon_night)

        val sharp = Sharp.loadAsset(context.assets, "weather_icons/${item.iconDay}.svg")
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
                    p5!!.color = context.resources.getColor(R.color.grey900)
                } else {
                    p5!!.color = context.resources.getColor(R.color.grey300)
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
        sharp.into(imageDay)

        val sharp2 = Sharp.loadAsset(context.assets, "weather_icons/${item.iconNight}.svg")
        sharp2.setOnElementListener(object : OnSvgElementListener {
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
                    p5!!.color = context.resources.getColor(R.color.grey900)
                } else {
                    p5!!.color = context.resources.getColor(R.color.grey300)
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
        sharp2.into(imagenight)
    }
}