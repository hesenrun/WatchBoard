package com.bqmz001.watchboard

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.bqmz001.watchboard.alarm.AlarmActivity
import com.bqmz001.watchboard.alarm.AlarmBean
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.litepal.LitePal

fun getWeekDay(dayOfWeek: Int): String {
    when (dayOfWeek) {
        1 -> return "星期一"
        2 -> return "星期二"
        3 -> return "星期三"
        4 -> return "星期四"
        5 -> return "星期五"
        6 -> return "星期六"
        7 -> return "星期日"
        else -> return ""
    }
}

fun getWeekDayQueryName(dayOfWeek: Int): String {
    when (dayOfWeek) {
        1 -> return "mondayenabled"
        2 -> return "tuesdayenabled"
        3 -> return "wednesdayenabled"
        4 -> return "thursdayenabled"
        5 -> return "fridayenabled"
        6 -> return "saturdayenabled"
        7 -> return "sundayenabled"
        else -> return ""
    }
}

fun dpToPixelValue(context: Context, dpValue: Int): Int {
    val scale: Float = context.getResources().getDisplayMetrics().density
    return (dpValue * scale + 0.5f).toInt()
}

fun startAlarm(context: Context, alarmTime: Long, delayMillis: Long, uuid: String) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent("bqmz001.alarm")
    intent.putExtra("uuid", uuid)
    val pendingIntent =
        PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    when {
        Build.VERSION.SDK_INT >= 26 -> {
            val pendingShowList =
                PendingIntent.getActivity(
                    context,
                    100,
                    Intent(context, AlarmActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(alarmTime, pendingShowList), pendingIntent
            )
        }
        Build.VERSION.SDK_INT >= 23 -> {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                pendingIntent
            )

        }
        Build.VERSION.SDK_INT >= 19 -> {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)

        }
        else -> {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
        }
    }
}


fun cancelAlarm(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent =
        PendingIntent.getBroadcast(
            context,
            100,
            Intent("bqmz001.alarm"),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    alarmManager.cancel(intent)
}

fun refreshAlarm(context: Context) {
    cancelAlarm(context)
    val nowTime = DateTime.now()
    val onceAlarmList = LitePal.where("isenabled = 1 and isonce = 1").order("hour,minute")
        .find(AlarmBean::class.java)
    if (onceAlarmList.size > 0)
        for (onceAlarm in onceAlarmList) {
            if (onceAlarm.hour > nowTime.hourOfDay || (onceAlarm.hour == nowTime.hourOfDay && onceAlarm.minute > nowTime.minuteOfHour)) {
                startAlarm(
                    context,
                    DateTime.now().withHourOfDay(onceAlarm.hour)
                        .withMinuteOfHour(onceAlarm.minute)
                        .withSecondOfMinute(0).withMillisOfSecond(0).millis,
                    5000,
                    onceAlarm.uuid
                )
            } else {
                startAlarm(
                    context,
                    DateTime.now().plusDays(1).withHourOfDay(onceAlarm.hour)
                        .withMinuteOfHour(onceAlarm.minute)
                        .withSecondOfMinute(0).withMillisOfSecond(0).millis,
                    5000,
                    onceAlarm.uuid
                )
            }
        }

    for (weekday: Int in nowTime.dayOfWeek..nowTime.dayOfWeek + 6) {
        var trueWeekDay = 0
        if (weekday > 7) {
            trueWeekDay = weekday % 7
        } else {
            trueWeekDay = weekday
        }
        val weekDayAlarmList =
            LitePal.where("isenabled = 1 and isonce = 0 and ${getWeekDayQueryName(trueWeekDay)} = 1")
                .order("hour,minute").find(AlarmBean::class.java)
        if (weekDayAlarmList.size == 0) {
            continue
        } else {
            for (weekDayAlarm in weekDayAlarmList) {
                if (trueWeekDay == nowTime.dayOfWeek && (weekDayAlarm.hour > nowTime.hourOfDay || (weekDayAlarm.hour == nowTime.hourOfDay && weekDayAlarm.minute > nowTime.minuteOfHour))) {
                    startAlarm(
                        context,
                        DateTime.now().withHourOfDay(weekDayAlarm.hour)
                            .withMinuteOfHour(weekDayAlarm.minute)
                            .withSecondOfMinute(0).withMillisOfSecond(0).millis,
                        5000,
                        weekDayAlarm.uuid
                    )
                } else if (trueWeekDay > nowTime.dayOfWeek) {
                    startAlarm(
                        context,
                        DateTime.now().withDayOfWeek(trueWeekDay)
                            .withHourOfDay(weekDayAlarm.hour)
                            .withMinuteOfHour(weekDayAlarm.minute)
                            .withSecondOfMinute(0).withMillisOfSecond(0).millis,
                        5000,
                        weekDayAlarm.uuid
                    )
                } else if (trueWeekDay < nowTime.dayOfWeek) {
                    startAlarm(
                        context,
                        DateTime.now().plusWeeks(1).withDayOfWeek(trueWeekDay)
                            .withHourOfDay(weekDayAlarm.hour)
                            .withMinuteOfHour(weekDayAlarm.minute)
                            .withSecondOfMinute(0).withMillisOfSecond(0).millis,
                        5000,
                        weekDayAlarm.uuid
                    )
                }
                break
            }
            break
        }
    }
}

fun getDataUpdateTimeMills(source:String):Long{
    var source2=source.replace("T"," ").replace("+08:00","").split(".")[0]
    val dateTime=DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(source2)
    return dateTime.millis
}
