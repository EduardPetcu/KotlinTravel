package com.example.travel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.travel.constants.NotificationConstants.NotificationKeys.NOTI_TITLE_KEY
import com.example.travel.service.TravelNotificationService

class ReminderReceiver: BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val scheduleNotificationService = context?.let { TravelNotificationService(it) }
        val title: String? = intent?.getStringExtra(NOTI_TITLE_KEY)
        scheduleNotificationService?.showBasicNotification(title)
    }
}