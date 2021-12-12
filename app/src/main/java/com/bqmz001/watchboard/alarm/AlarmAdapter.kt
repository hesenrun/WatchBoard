package com.bqmz001.watchboard.alarm

import android.content.res.ColorStateList
import android.view.View
import com.bqmz001.watchboard.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class AlarmAdapter(list: MutableList<AlarmBean>) :
    BaseQuickAdapter<AlarmBean, BaseViewHolder>(R.layout.item_alarm, list) {
    override fun convert(holder: BaseViewHolder, item: AlarmBean) {
        holder.setText(R.id.tv_hour, String.format("%02d", item.hour))
            .setText(R.id.tv_minute, String.format("%02d", item.minute))

        val dot1: View = holder.getView<View>(R.id.dot1)
        val dot2: View = holder.getView<View>(R.id.dot2)
        val dot3: View = holder.getView<View>(R.id.dot3)
        val dot4: View = holder.getView<View>(R.id.dot4)
        val dot5: View = holder.getView<View>(R.id.dot5)
        val dot6: View = holder.getView<View>(R.id.dot6)
        val dot7: View = holder.getView<View>(R.id.dot7)
        val dot8: View = holder.getView<View>(R.id.dot8)
        val lineEnabled: View = holder.getView<View>(R.id.line_enabled)

        if (item.isEnabled) {
            lineEnabled.setBackgroundColor(context.resources.getColor(R.color.green500))
        } else {
            lineEnabled.setBackgroundColor(context.resources.getColor(R.color.blue_grey500))
        }

        if (item.isOnce) {
            dot8.backgroundTintList =
                ColorStateList.valueOf(context.resources.getColor(R.color.yellow500))
            dot8.visibility = View.VISIBLE
            dot1.visibility = View.GONE
            dot2.visibility = View.GONE
            dot3.visibility = View.GONE
            dot4.visibility = View.GONE
            dot5.visibility = View.GONE
            dot6.visibility = View.GONE
            dot7.visibility = View.GONE
        } else {
            dot8.backgroundTintList =
                ColorStateList.valueOf(context.resources.getColor(R.color.blue_grey500))
            dot8.visibility = View.GONE
            dot1.visibility = View.VISIBLE
            dot2.visibility = View.VISIBLE
            dot3.visibility = View.VISIBLE
            dot4.visibility = View.VISIBLE
            dot5.visibility = View.VISIBLE
            dot6.visibility = View.VISIBLE
            dot7.visibility = View.VISIBLE
        }

        dot1.backgroundTintList = ColorStateList.valueOf(
            context.resources.getColor(
                when (item.isMondayEnabled) {
                    true -> R.color.green500
                    false -> R.color.blue_grey500
                }
            )
        )
        dot2.backgroundTintList = ColorStateList.valueOf(
            context.resources.getColor(
                when (item.isTuesdayEnabled) {
                    true -> R.color.green500
                    false -> R.color.blue_grey500
                }
            )
        )
        dot3.backgroundTintList = ColorStateList.valueOf(
            context.resources.getColor(
                when (item.isWednesdayEnabled) {
                    true -> R.color.green500
                    false -> R.color.blue_grey500
                }
            )
        )
        dot4.backgroundTintList = ColorStateList.valueOf(
            context.resources.getColor(
                when (item.isThursdayEnabled) {
                    true -> R.color.green500
                    false -> R.color.blue_grey500
                }
            )
        )
        dot5.backgroundTintList = ColorStateList.valueOf(
            context.resources.getColor(
                when (item.isFridayEnabled) {
                    true -> R.color.green500
                    false -> R.color.blue_grey500
                }
            )
        )
        dot6.backgroundTintList = ColorStateList.valueOf(
            context.resources.getColor(
                when (item.isSaturdayEnabled) {
                    true -> R.color.green500
                    false -> R.color.blue_grey500
                }
            )
        )
        dot7.backgroundTintList = ColorStateList.valueOf(
            context.resources.getColor(
                when (item.isSundayEnabled) {
                    true -> R.color.green500
                    false -> R.color.blue_grey500
                }
            )
        )


    }
}