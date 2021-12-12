package com.bqmz001.watchboard.calendar

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.bqmz001.watchboard.R
import com.bqmz001.watchboard.databinding.ActivityCalendarBinding
import com.bqmz001.watchboard.dpToPixelValue
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.haibin.calendarview.MonthView

class CalendarActivity : AppCompatActivity() {

    lateinit var binding: ActivityCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivMore.setOnClickListener {
            binding.calendar.scrollToCurrent()
        }
        binding.tvMonth.setText("${binding.calendar.curYear} 年 ${binding.calendar.curMonth} 月")

        binding.calendar.setOnMonthChangeListener { year, month ->
            binding.tvMonth.setText("${year} 年 ${month} 月")
        }
        binding.calendar.setOnCalendarSelectListener(object :
            CalendarView.OnCalendarSelectListener {
            override fun onCalendarOutOfRange(calendar: Calendar?) {

            }

            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                if (isClick) {
                    val intent = Intent(this@CalendarActivity, CalendarDetailActivity::class.java)
                    intent.putExtra("date", "${calendar!!.year}-${calendar.month}-${calendar.day}")
                    startActivity(intent)
                }
            }

        })

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode==219){
            finish()
        }
        return true
    }
}

class MeizuMonthView(context: Context?) : MonthView(context) {
    private val mSchemeBasicPaint = Paint()
    private var mRadio = 0f
    private var mPadding = 0
    private var mSchemeBaseLine = 0f

    init {
        mSchemeBasicPaint.isAntiAlias = true
        mSchemeBasicPaint.style = Paint.Style.FILL
        mSchemeBasicPaint.textAlign = Paint.Align.CENTER
        mSchemeBasicPaint.isFakeBoldText = true
        mRadio =
            dpToPixelValue(getContext(), 7).toFloat()
        mPadding = dpToPixelValue(getContext(), 4)
        val metrics = mSchemeBasicPaint.fontMetrics
        mSchemeBaseLine =
            mRadio - metrics.descent + (metrics.bottom - metrics.top) / 2 + dpToPixelValue(
                getContext(),
                1
            )
    }

    override fun onDrawSelected(
        canvas: Canvas?,
        calendar: Calendar?,
        x: Int,
        y: Int,
        hasScheme: Boolean
    ): Boolean {
        mSelectedPaint.style = Paint.Style.FILL
        mSelectedPaint.color = context.resources.getColor(R.color.red300)
        canvas!!.drawRoundRect(
            (x + 16).toFloat(),
            y.toFloat(),
            (x + mItemWidth - 16).toFloat(),
            (y + mItemHeight).toFloat(),
            12f, 12f,
            mSelectedPaint
        )
        return false
    }

    override fun onDrawScheme(canvas: Canvas?, calendar: Calendar?, x: Int, y: Int) {

    }

    override fun onDrawText(
        canvas: Canvas?,
        calendar: Calendar?,
        x: Int,
        y: Int,
        hasScheme: Boolean,
        isSelected: Boolean
    ) {
        val cx = x + mItemWidth / 2
        val top = y - mItemHeight / 6

        val isInRange = isInRange(calendar)

        if (isSelected) {
            mSelectTextPaint.color = context.resources.getColor(R.color.white)
            mSelectedLunarTextPaint.color = context.resources.getColor(R.color.white)
            canvas!!.drawText(
                calendar!!.day.toString(), cx.toFloat(), mTextBaseLine + top - 5,
                mSelectTextPaint
            )
            canvas.drawText(
                calendar.lunar,
                cx.toFloat(),
                mTextBaseLine + y + mItemHeight / 10 + 2,
                mSelectedLunarTextPaint
            )
        } else if (hasScheme) {
            mSelectTextPaint.color = context.resources.getColor(R.color.black)
            mSelectedLunarTextPaint.color = context.resources.getColor(R.color.grey500)
            canvas!!.drawText(
                calendar!!.day.toString(), cx.toFloat(), mTextBaseLine + top - 5,
                if (calendar.isCurrentMonth && isInRange) mSchemeTextPaint else mOtherMonthTextPaint
            )
            canvas.drawText(
                calendar.lunar,
                cx.toFloat(),
                mTextBaseLine + y + mItemHeight / 10 + 2,
                mCurMonthLunarTextPaint
            )
        } else {
            mSelectTextPaint.color = context.resources.getColor(R.color.black)
            mSelectedLunarTextPaint.color = context.resources.getColor(R.color.grey500)
            canvas!!.drawText(
                calendar!!.day.toString(), cx.toFloat(), mTextBaseLine + top - 5,
                if (calendar.isCurrentDay) mCurDayTextPaint else if (calendar.isCurrentMonth && isInRange) mCurMonthTextPaint else mOtherMonthTextPaint
            )
            canvas.drawText(
                calendar.lunar, cx.toFloat(), mTextBaseLine + y + mItemHeight / 10 + 2,
                if (calendar.isCurrentDay && isInRange) mCurDayLunarTextPaint else if (calendar.isCurrentMonth) mCurMonthLunarTextPaint else mOtherMonthLunarTextPaint
            )
        }
    }

}