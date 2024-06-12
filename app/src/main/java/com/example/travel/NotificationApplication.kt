package com.example.travel;
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.travel.constants.NotificationConstants.NotificationKeys.NOTI_CHNNL_ID
import com.example.travel.constants.NotificationConstants.NotificationKeys.NOTI_CHNNL_NAME

class NotificationApplication : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val notificationChannel = NotificationChannel(
            NOTI_CHNNL_ID,
            NOTI_CHNNL_NAME,
            NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}
