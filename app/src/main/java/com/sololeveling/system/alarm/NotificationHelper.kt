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

            // Daily Quests Channel
            val dailyChannel = NotificationChannel(
                CHANNEL_DAILY_QUEST,
                "Daily Quests & Awakening",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "High-priority anime notifications for incoming daily workout quests"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 250, 150, 250)
            }

            // Penalty Warning Channel
            val penaltyChannel = NotificationChannel(
                CHANNEL_PENALTY_WARNING,
                "Penalty Quest Warnings",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Critical alert before midnight when daily quests remain incomplete"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500, 200, 500)
            }

            // General System Channel
            val systemChannel = NotificationChannel(
                CHANNEL_SYSTEM_ALERTS,
                "System Achievements & Level Up",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Level up announcements and item drop alerts"
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
            .setContentTitle("⚔️ [QUEST ARRIVED: DAILY TRAINING]")
            .setContentText("Physical preparation to become strong is now active! Complete before midnight.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("[QUEST: DAILY TRAINING]\n• Push-ups\n• Sit-ups\n• Squats\n• Running / Steps\n\n⚠️ Warning: Incomplete daily quests will transport the Player to the Penalty Zone!")
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
            .setContentTitle("🚨 [WARNING: PENALTY QUEST IMMINENT]")
            .setContentText("Less than 2 hours remaining! Complete your daily workout immediately!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("[SYSTEM ALERT]\nPlayer has not completed today's daily quest.\nFailure to comply before midnight will result in immediate penalty quest dispatch: [SURVIVE IN THE CENTIPEDE DESERT FOR 4 HOURS].")
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
            .setContentTitle("👑 [SYSTEM: LEVEL UP!]")
            .setContentText("Congratulations Player! You have reached LEVEL $newLevel!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Level Up achieved!\nMax HP and Max MP increased.\n+3 Unallocated Stat Points have been deposited into your status window.")
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
