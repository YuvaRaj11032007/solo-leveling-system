package com.sololeveling.system.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sololeveling.system.MainActivity
import com.sololeveling.system.R

object NotificationHelper {

    const val CHANNEL_DAILY_QUEST = "channel_daily_quest"
    const val CHANNEL_PENALTY_WARNING = "channel_penalty_warning"
    const val CHANNEL_SYSTEM_ALERTS = "channel_system_alerts"

    const val NOTIFICATION_ID_DAILY = 1001
    const val NOTIFICATION_ID_PENALTY = 1002
    const val NOTIFICATION_ID_LEVEL_UP = 1003

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val dailyChannel = NotificationChannel(
                CHANNEL_DAILY_QUEST,
                "Daily Discipline & Protocols",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "High-priority notifications for daily calisthenics, hydration & deep work protocols"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 250, 150, 250)
            }

            val penaltyChannel = NotificationChannel(
                CHANNEL_PENALTY_WARNING,
                "Discipline Warning Radar",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Critical alert before midnight when daily protocols remain incomplete"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500, 200, 500)
            }

            val systemChannel = NotificationChannel(
                CHANNEL_SYSTEM_ALERTS,
                "System Achievements & Level Up",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Level up announcements and operational milestone alerts"
            }

            notificationManager.createNotificationChannel(dailyChannel)
            notificationManager.createNotificationChannel(penaltyChannel)
            notificationManager.createNotificationChannel(systemChannel)
        }
    }

    fun showDailyQuestNotification(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_DAILY_QUEST)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("⚡ [SYSTEM PROTOCOL: DAILY PERFORMANCE]")
            .setContentText("Physical & Cognitive protocols are now active! Complete before midnight.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("[DAILY PERFORMANCE PROTOCOLS]\n• Calisthenics (Push-ups / Squats / Core)\n• Aerobic Movement & Paces\n• Optimal Hydration (3,000 ml)\n• Deep Cognitive Focus & Reading\n\nMaintain discipline. Strive for baseline excellence.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_DAILY, notification)
        } catch (_: SecurityException) {
            // Permission not granted
        }
    }

    fun showPenaltyWarningNotification(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_PENALTY_WARNING)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("🚨 [WARNING: PROTOCOL DEADLINE APPROACHING]")
            .setContentText("Less than 2 hours remaining! Finish your daily discipline targets immediately.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("[SYSTEM DISCIPLINE RADAR]\nOperator has pending daily protocols.\nUncompleted tasks at midnight trigger [DISCIPLINE DEFICIT: +25% volume penalty tomorrow and streak reset]. Execute now.")
            )
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_PENALTY, notification)
        } catch (_: SecurityException) {
            // Permission not granted
        }
    }

    fun showLevelUpNotification(context: Context, newLevel: Int) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_SYSTEM_ALERTS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("👑 [SYSTEM: OPERATOR LEVEL UP!]")
            .setContentText("Congratulations Operator! You have reached LEVEL $newLevel!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Performance Level Up achieved!\nMax Stamina and Cognitive Bandwidth increased.\n+3 Attribute Points available for distribution.")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_LEVEL_UP, notification)
        } catch (_: SecurityException) {
            // Permission not granted
        }
    }
}
