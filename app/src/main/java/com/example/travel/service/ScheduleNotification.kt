package com.example.travel.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import com.example.travel.ReminderReceiver
import com.example.travel.constants.NotificationConstants.NotificationKeys.NOTI_TITLE_KEY

class ScheduleNotification {
    fun scheduleNotification(
        context: Context,
        title: String,
        stopDate: String,
        budgetId: String
    ) {
        val intent = Intent(context.applicationContext, ReminderReceiver::class.java)
        intent.putExtra(NOTI_TITLE_KEY, title)
        intent.putExtra("budgetId", budgetId)
        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            budgetId.hashCode(),
            intent,
            PendingIntent.FLAG_MUTABLE
        )
        val stopDateArray = stopDate.split("-")
        Log.d("ScheduleNotification", "BudgetID: $budgetId")
        val stopCalendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, stopDateArray[0].toInt() - 1) // remind the user the day before the budget expires
            set(Calendar.MONTH, stopDateArray[1].toInt() - 1)
            set(Calendar.YEAR, stopDateArray[2].toInt())
            set(Calendar.HOUR_OF_DAY, 18) // remind the user with 6 hours before the budget expires
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, stopCalendar.timeInMillis, pendingIntent)
    }

    fun cancelNotification(context: Context, budgetId: String) {
        val intent = Intent(context.applicationContext, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            budgetId.hashCode(),
            intent,
            PendingIntent.FLAG_MUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

    }
}