package com.bqmz001.watchboard.settings

import com.bqmz001.watchboard.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.qweather.sdk.bean.weather.WeatherDailyBean

class CitySelectAdapter(list: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(
        R.layout.item_city,
        list
    ) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_city, item)
    }
}