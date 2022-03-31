package com.bqmz001.watchboard.alarm

import android.annotation.SuppressLint
import android.content.*
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.bqmz001.watchboard.R
import com.bqmz001.watchboard.databinding.ActivityAlarmRingBinding
import com.bqmz001.watchboard.refreshAlarm
import com.orhanobut.hawk.Hawk
import org.joda.time.DateTime
import org.litepal.LitePal

class AlarmRingActivity : AppCompatActivity() {
    lateinit var binding: ActivityAlarmRingBinding
    lateinit var receiver: TimeReceiver
    lateinit var mediaPlayer: MediaPlayer

    companion object {
        var plusMinute: Int = 0
        var maxMinute: Int = 0
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        plusMinute=0
        binding = ActivityAlarmRingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        maxMinute = Hawk.get("alarm_long", 10)
        val alarm =
            LitePal.where("uuid = ?", intent.getStringExtra("uuid")).find(AlarmBean::class.java)[0]
        binding.tvAlarmTime.setText(
            "${String.format("%02d", alarm.hour)}:${
                String.format(
                    "%02d",
                    alarm.minute
                )
            }"
        )
        if (alarm.isOnce) {
            val contentValues = ContentValues()
            contentValues.put("isenabled", 0)
            LitePal.updateAll(
                AlarmBean::class.java,
                contentValues,
                "uuid = ?",
                intent.getStringExtra("uuid").toString()
            )
        }
        refreshAlarm(this)
        turnScreenOn()
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_ring)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        receiver = TimeReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_TICK)
        registerReceiver(receiver, filter)



        binding.cvFinish.setOnClickListener {
            mediaPlayer.pause()
            mediaPlayer.release()
            unregisterReceiver(receiver)
            Toast.makeText(applicationContext, "在闹钟响起${plusMinute}分钟后您手动关闭了闹钟", Toast.LENGTH_LONG).show()
            plusMinute=0
            finish()
        }

        refreshTime()
    }

    inner class TimeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent!!.action.equals(Intent.ACTION_TIME_TICK)) {
                refreshTime()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun refreshTime() {
        val dateTime = DateTime.now()
        binding.tvNowTime.setText(
            "${
                String.format(
                    "%02d",
                    dateTime.hourOfDay
                )
            }:${String.format("%02d", dateTime.minuteOfHour)}"
        )
        if (!binding.tvNowTime.text.equals(binding.tvAlarmTime.text)) {
            plusMinute++
        }
        if (plusMinute<=99){
            binding.tvPlusTime.setText("+${plusMinute}m")
        }else{
            Toast.makeText(this, "这到底是叫不醒了还是家里没人？", Toast.LENGTH_LONG).show()
            binding.tvPlusTime.setText("+??m")
        }
        if (maxMinute != 0 && plusMinute > maxMinute) {
            mediaPlayer.pause()
            mediaPlayer.release()
            unregisterReceiver(receiver)
            Toast.makeText(applicationContext, "在闹钟响起${plusMinute-1}分钟后被系统自动关闭闹钟", Toast.LENGTH_LONG).show()
            plusMinute=0
            finish()
        }
    }

    fun turnScreenOn() {
        if (Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        // Deprecated flags are required on some devices, even with API>=27
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
    }

}