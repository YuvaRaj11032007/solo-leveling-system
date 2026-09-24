package com.sololeveling.system.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

object AlarmScheduler {

    const val ACTION_DAILY_ALARM = "com.sololeveling.system.ACTION_DAILY_ALARM"
    const val ACTION_PENALTY_WARNING = "com.sololeveling.system.ACTION_PENALTY_WARNING"

    private const val REQUEST_CODE_DAILY = 2001
    private const val REQUEST_CODE_PENALTY = 2002

    fun scheduleDailyQuestAlarm(context: Context, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_DAILY_ALARM
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_DAILY,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (_: SecurityException) {
            // In Android 12+, exact alarm permission might need user consent
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun schedulePenaltyWarning(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_PENALTY_WARNING
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_PENALTY,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set to 22:00 (10:00 PM) - 2 hours before midnight reset
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 22)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (_: SecurityException) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelAlarms(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val dailyIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_DAILY_ALARM
        }
        val dailyPendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_DAILY,
            dailyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(dailyPendingIntent)

        val penaltyIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_PENALTY_WARNING
        }
        val penaltyPendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_PENALTY,
            penaltyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(penaltyPendingIntent)
    }
}
