package com.bqmz001.watchboard.alarm

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.bqmz001.watchboard.databinding.ActivityAlarmEditBinding
import org.joda.time.DateTime
import org.litepal.LitePal
import org.litepal.extension.find
import java.util.*

class AlarmEditActivity : AppCompatActivity() {

    lateinit var binding: ActivityAlarmEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivClose.setOnClickListener { finish() }
        binding.ivDelete.setOnClickListener {
            val alarm = LitePal.where("uuid = ?", intent.getStringExtra("id").toString())
                .find<AlarmBean>()[0]
            LitePal.delete(AlarmBean::class.java, alarm.id.toLong())
            setResult(RESULT_OK)
            finish()
        }
        binding.ivSubmit.setOnClickListener {
            if (!binding.swOnlyOnce.isChecked && !binding.checkBox.isChecked && !binding.checkBox2.isChecked && !binding.checkBox3.isChecked && !binding.checkBox4.isChecked && !binding.checkBox5.isChecked && !binding.checkBox6.isChecked && !binding.checkBox7.isChecked) {
                Toast.makeText(this, "请选择闹钟响铃时间", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (intent.getStringExtra("type").equals("add")) {
                val alarm = AlarmBean()
                alarm.uuid = UUID.randomUUID().toString()
                alarm.hour = binding.tvAlarmHour.text.toString().toInt()
                alarm.minute = binding.tvAlarmMinute.text.toString().toInt()
                alarm.isEnabled = binding.swEnabled.isChecked
                alarm.isOnce = binding.swOnlyOnce.isChecked
                if (!binding.swOnlyOnce.isChecked) {
                    alarm.isMondayEnabled = binding.checkBox.isChecked
                    alarm.isTuesdayEnabled = binding.checkBox2.isChecked
                    alarm.isWednesdayEnabled = binding.checkBox3.isChecked
                    alarm.isThursdayEnabled = binding.checkBox4.isChecked
                    alarm.isFridayEnabled = binding.checkBox5.isChecked
                    alarm.isSaturdayEnabled = binding.checkBox6.isChecked
                    alarm.isSundayEnabled = binding.checkBox7.isChecked
                }
                alarm.save()
            } else if (intent.getStringExtra("type").equals("edit")) {
                val contentValues = ContentValues()
                contentValues.put("hour", binding.tvAlarmHour.text.toString().toInt())
                contentValues.put("minute", binding.tvAlarmMinute.text.toString().toInt())
                contentValues.put(
                    "isenabled", when (binding.swEnabled.isChecked) {
                        true -> 1
                        false -> 0
                    }
                )
                contentValues.put(
                    "isonce", when (binding.swOnlyOnce.isChecked) {
                        true -> 1
                        false -> 0
                    }
                )
                contentValues.put(
                    "mondayenabled", when (binding.swOnlyOnce.isChecked) {
                        true -> 0
                        false -> when (binding.checkBox.isChecked) {
                            true -> 1
                            false -> 0
                        }
                    }
                )
                contentValues.put(
                    "tuesdayenabled", when (binding.swOnlyOnce.isChecked) {
                        true -> 0
                        false -> when (binding.checkBox2.isChecked) {
                            true -> 1
                            false -> 0
                        }
                    }
                )
                contentValues.put(
                    "wednesdayenabled", when (binding.swOnlyOnce.isChecked) {
                        true -> 0
                        false -> when (binding.checkBox3.isChecked) {
                            true -> 1
                            false -> 0
                        }
                    }
                )
                contentValues.put(
                    "thursdayenabled", when (binding.swOnlyOnce.isChecked) {
                        true -> 0
                        false -> when (binding.checkBox4.isChecked) {
                            true -> 1
                            false -> 0
                        }
                    }
                )
                contentValues.put(
                    "fridayenabled", when (binding.swOnlyOnce.isChecked) {
                        true -> 0
                        false -> when (binding.checkBox5.isChecked) {
                            true -> 1
                            false -> 0
                        }
                    }
                )
                contentValues.put(
                    "saturdayenabled", when (binding.swOnlyOnce.isChecked) {
                        true -> 0
                        false -> when (binding.checkBox6.isChecked) {
                            true -> 1
                            false -> 0
                        }
                    }
                )
                contentValues.put(
                    "sundayenabled", when (binding.swOnlyOnce.isChecked) {
                        true -> 0
                        false -> when (binding.checkBox7.isChecked) {
                            true -> 1
                            false -> 0
                        }
                    }
                )

                LitePal.updateAll(
                    AlarmBean::class.java,
                    contentValues,
                    "uuid = ?",
                    intent.getStringExtra("id").toString()
                )
            }
            setResult(RESULT_OK)
            finish()
        }

        binding.ivHourUp.setOnClickListener {
            var hour = binding.tvAlarmHour.text.toString().toInt()
            if (hour == 23) {
                hour = 0
            } else {
                hour += 1
            }
            binding.tvAlarmHour.setText(String.format("%02d", hour))
        }
        binding.ivHourDown.setOnClickListener {
            var hour = binding.tvAlarmHour.text.toString().toInt()
            if (hour == 0) {
                hour = 23
            } else {
                hour -= 1
            }
            binding.tvAlarmHour.setText(String.format("%02d", hour))
        }
        binding.ivMinuteUp.setOnClickListener {
            var minute = binding.tvAlarmMinute.text.toString().toInt()
            if (minute == 59) {
                minute = 0
            } else {
                minute += 1
            }
            binding.tvAlarmMinute.setText(String.format("%02d", minute))
        }
        binding.ivMinuteDown.setOnClickListener {
            var minute = binding.tvAlarmMinute.text.toString().toInt()
            if (minute == 0) {
                minute = 59
            } else {
                minute -= 1
            }
            binding.tvAlarmMinute.setText(String.format("%02d", minute))
        }
        binding.swOnlyOnce.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.checkBox.isEnabled = false
                binding.checkBox2.isEnabled = false
                binding.checkBox3.isEnabled = false
                binding.checkBox4.isEnabled = false
                binding.checkBox5.isEnabled = false
                binding.checkBox6.isEnabled = false
                binding.checkBox7.isEnabled = false
            } else {
                binding.checkBox.isEnabled = true
                binding.checkBox2.isEnabled = true
                binding.checkBox3.isEnabled = true
                binding.checkBox4.isEnabled = true
                binding.checkBox5.isEnabled = true
                binding.checkBox6.isEnabled = true
                binding.checkBox7.isEnabled = true
            }
        }

        if (intent.getStringExtra("type").equals("edit")) {
            binding.ivDelete.visibility = View.VISIBLE
            val alarm = LitePal.where("uuid = ?", intent.getStringExtra("id").toString())
                .find<AlarmBean>()[0]
            binding.tvAlarmHour.setText(String.format("%02d", alarm.hour))
            binding.tvAlarmMinute.setText(String.format("%02d", alarm.minute))
            binding.swEnabled.isChecked = alarm.isEnabled
            binding.swOnlyOnce.isChecked = alarm.isOnce
            if (alarm.isOnce) {
                binding.checkBox.isEnabled = false
                binding.checkBox2.isEnabled = false
                binding.checkBox3.isEnabled = false
                binding.checkBox4.isEnabled = false
                binding.checkBox5.isEnabled = false
                binding.checkBox6.isEnabled = false
                binding.checkBox7.isEnabled = false

                binding.checkBox.isChecked = false
                binding.checkBox2.isChecked = false
                binding.checkBox3.isChecked = false
                binding.checkBox4.isChecked = false
                binding.checkBox5.isChecked = false
                binding.checkBox6.isChecked = false
                binding.checkBox7.isChecked = false
            } else {
                binding.checkBox.isEnabled = true
                binding.checkBox2.isEnabled = true
                binding.checkBox3.isEnabled = true
                binding.checkBox4.isEnabled = true
                binding.checkBox5.isEnabled = true
                binding.checkBox6.isEnabled = true
                binding.checkBox7.isEnabled = true

                binding.checkBox.isChecked = alarm.isMondayEnabled
                binding.checkBox2.isChecked = alarm.isTuesdayEnabled
                binding.checkBox3.isChecked = alarm.isWednesdayEnabled
                binding.checkBox4.isChecked = alarm.isThursdayEnabled
                binding.checkBox5.isChecked = alarm.isFridayEnabled
                binding.checkBox6.isChecked = alarm.isSaturdayEnabled
                binding.checkBox7.isChecked = alarm.isSundayEnabled
            }


        } else {
            binding.ivDelete.visibility = View.GONE
            val date = DateTime.now()
            binding.tvAlarmHour.setText(String.format("%02d", date.hourOfDay))
            binding.tvAlarmMinute.setText(String.format("%02d", date.minuteOfHour))
            binding.swEnabled.isChecked = true
            binding.swOnlyOnce.isChecked = false

            binding.checkBox.isEnabled = true
            binding.checkBox2.isEnabled = true
            binding.checkBox3.isEnabled = true
            binding.checkBox4.isEnabled = true
            binding.checkBox5.isEnabled = true
            binding.checkBox6.isEnabled = true
            binding.checkBox7.isEnabled = true

            binding.checkBox.isChecked = true
            binding.checkBox2.isChecked = true
            binding.checkBox3.isChecked = true
            binding.checkBox4.isChecked = true
            binding.checkBox5.isChecked = true
            binding.checkBox6.isChecked = false
            binding.checkBox7.isChecked = false
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == 219) {
            finish()
        }
        return true
    }
}