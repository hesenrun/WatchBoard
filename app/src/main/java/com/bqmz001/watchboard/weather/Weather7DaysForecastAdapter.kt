package com.bqmz001.watchboard.weather

import android.widget.ImageView
import com.bqmz001.watchboard.R
import com.bqmz001.watchboard.getWeekDay
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.pixplicity.sharp.Sharp
import com.qweather.sdk.bean.weather.WeatherDailyBean
import org.joda.time.format.DateTimeFormat

class Weather7DaysForecastAdapter(list: MutableList<WeatherDailyBean.DailyBean>) :
    BaseQuickAdapter<WeatherDailyBean.DailyBean, BaseViewHolder>(
        R.layout.item_7_day_forecast,
        list
    ) {
    override fun convert(holder: BaseViewHolder, item: WeatherDailyBean.DailyBean) {

        holder.setText(R.id.tv_time, "${item.fxDate.substring(5, 10)}\n${getWeekDay(DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(item.fxDate.substring(0, 10)).dayOfWeek)}")
            .setText(R.id.tv_temp_day, item.tempMax+"°C")
            .setText(R.id.tv_temp_night, item.tempMin+"°C")

        val imageDay = holder.getView<ImageView>(R.id.iv_weather_icon_day)
        val imagenight = holder.getView<ImageView>(R.id.iv_weather_icon_night)
        Sharp.loadAsset(context.assets, "weather_icons/${item.iconDay}.svg")
            .into(imageDay)
        Sharp.loadAsset(context.assets, "weather_icons/${item.iconNight}.svg")
            .into(imagenight)
    }
}