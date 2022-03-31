package com.bqmz001.watchboard.settings

import android.content.Context
import android.media.AudioManager
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.bqmz001.watchboard.R
import com.lxj.xpopup.core.CenterPopupView

class AlarmVolumeDialog(context: Context) : CenterPopupView(context) {

    companion object {
        lateinit var manager: AudioManager
    }

    override fun getImplLayoutId() = R.layout.dialog_volume

    override fun onCreate() {
        super.onCreate()
        manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val imageVolume = findViewById<ImageView>(R.id.image_volume)
        val imageDismiss = findViewById<ImageView>(R.id.image_dismiss)
        val seekBar = findViewById<SeekBar>(R.id.seek)

        seekBar.max = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        seekBar.progress = manager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val nowValue = seekBar.progress
        if (nowValue >= (manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2)) {
            imageVolume.setImageResource(R.drawable.ic_baseline_volume_up_24)
        } else if (nowValue < (manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2) && nowValue > 0) {
            imageVolume.setImageResource(R.drawable.ic_baseline_volume_down_24)
        } else {
            imageVolume.setImageResource(R.drawable.ic_baseline_volume_mute_24)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p1 >= (manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2)) {
                    imageVolume.setImageResource(R.drawable.ic_baseline_volume_up_24)
                } else if (p1 < (manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2) && p1 > 0) {
                    imageVolume.setImageResource(R.drawable.ic_baseline_volume_down_24)
                } else {
                    imageVolume.setImageResource(R.drawable.ic_baseline_volume_mute_24)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                val nowValue = p0!!.progress
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, nowValue, 0)
                if (nowValue == 0) Toast.makeText(context, "没声音这闹钟可不兴响啊~", Toast.LENGTH_LONG).show()
            }
        })

        imageDismiss.setOnClickListener {
            dismiss()
        }

    }

}