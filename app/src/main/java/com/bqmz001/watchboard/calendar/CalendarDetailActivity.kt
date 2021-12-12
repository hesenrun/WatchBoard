package com.bqmz001.watchboard.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.bqmz001.watchboard.databinding.ActivityCalendarDetailBinding
import com.bqmz001.watchboard.getWeekDay
import com.nlf.calendar.Lunar
import org.joda.time.format.DateTimeFormat

class CalendarDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityCalendarDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener { finish() }

        val dateTime =
            DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(intent.getStringExtra("date"))
        binding.tvDate.setText(
            "${dateTime.year}年 ${dateTime.monthOfYear}月 ${dateTime.dayOfMonth}日 ${
                getWeekDay(
                    dateTime.dayOfWeek
                )
            }"
        )
        val lunar = Lunar.fromDate(dateTime.toDate())
        binding.tvLunarDate.setText("${lunar.yearInGanZhi}(${lunar.yearShengXiao})年 ${lunar.monthInGanZhi}月 ${lunar.dayInGanZhi}日")
        binding.tvWuxing.setText("五行：${lunar.dayNaYin}")
        binding.tvChongsha.setText("冲煞：冲${lunar.dayChongDesc} 煞${lunar.daySha}")
        binding.tvPengzu.setText("彭祖百忌：${lunar.pengZuGan}  ${lunar.pengZuZhi}")
        binding.tvShen.setText("喜神：${lunar.dayPositionXiDesc} 福神：${lunar.dayPositionFuDesc} 财神：${lunar.dayPositionCaiDesc}")

        var yiString = ""
        for (s in lunar.dayYi) {
            yiString+="${s} "
        }
        var jiString =""
        for (s in lunar.dayJi){
            jiString+="${s} "
        }
        binding.tvYi.setText("宜：${yiString}")
        binding.tvJi.setText("忌：${jiString}")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode==219){
            finish()
        }
        return true
    }
}