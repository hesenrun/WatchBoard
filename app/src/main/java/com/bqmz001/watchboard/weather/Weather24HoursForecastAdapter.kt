package com.bqmz001.watchboard.weather

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.widget.ImageView
import com.bqmz001.watchboard.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.orhanobut.hawk.Hawk
import com.pixplicity.sharp.OnSvgElementListener
import com.pixplicity.sharp.Sharp
import com.qweather.sdk.bean.weather.WeatherHourlyBean

class Weather24HoursForecastAdapter(list: MutableList<WeatherHourlyBean.HourlyBean>) :
    BaseQuickAdapter<WeatherHourlyBean.HourlyBean, BaseViewHolder>(
        R.layout.item_24_hour_forecast,
        list
    ) {
    override fun convert(holder: BaseViewHolder, item: WeatherHourlyBean.HourlyBean) {
        holder.setText(R.id.tv_time, item.fxTime.substring(11,16))
            .setText(R.id.tv_weather_name,item.text)
            .setText(R.id.tv_temp,item.temp+"°C")
            .setText(R.id.tv_wind,"${item.windDir} ${item.windScale}级")
        val image=holder.getView<ImageView>(R.id.iv_weather_icon)

        val sharp = Sharp.loadAsset(context.assets, "weather_icons/${item.icon}.svg")
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
        sharp.into(image)
    }
}