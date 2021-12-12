package com.bqmz001.watchboard.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val ring = Intent(context, AlarmRingActivity::class.java)
        ring.putExtra("uuid", intent.getStringExtra("uuid").toString())
        context.startActivity(ring)
    }
}