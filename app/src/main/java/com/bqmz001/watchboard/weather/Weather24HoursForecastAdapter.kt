package com.bqmz001.watchboard.weather

import android.widget.ImageView
import com.bqmz001.watchboard.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
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
        Sharp.loadAsset(context.assets, "weather_icons/${item.icon}.svg")
            .into(image)
    }
}