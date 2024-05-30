package com.example.travel.service;

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.travel.R
import com.example.travel.constants.NotificationConstants.NotificationKeys.NOTI_CHNNL_ID
import com.example.travel.constants.NotificationConstants.NotificationKeys.NOTI_ID
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
class TravelNotificationService (private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    init {
        createNotificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Travel Notification Channel"
            val descriptionText = "Channel for Travel notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTI_CHNNL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
    fun showBasicNotification(title: String?) {
        val notification = NotificationCompat.Builder(context, NOTI_CHNNL_ID)
            .setContentTitle(title)
            .setContentText("Your budget is about to expire")
            .setSmallIcon(R.drawable.travellogo2)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        Log.d("Notification", "Notification sent")
        notificationManager.notify(NOTI_ID, notification)
    }

    fun showExpandableNotification(title: String?) {
        val notification = NotificationCompat.Builder(context, NOTI_CHNNL_ID)
            .setContentTitle(title)
            .setContentText("Your budget is about to expire")
            .setSmallIcon(R.drawable.travellogo2)
            .setStyle(NotificationCompat.BigTextStyle().bigText("This is a big text notification"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat
                    .BigPictureStyle()
                    .bigPicture(
                        context.bitmapFromResource(R.drawable.travellogo2
                        )
                    )
            )
            .build()
        notificationManager.notify(NOTI_ID, notification)
    }

    private fun Context.bitmapFromResource(
        @DrawableRes resId:Int
    )= BitmapFactory.decodeResource(
        resources,
        resId
    )
}
