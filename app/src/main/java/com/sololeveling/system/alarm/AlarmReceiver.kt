package com.sololeveling.system.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sololeveling.system.data.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AlarmScheduler.ACTION_DAILY_ALARM -> {
                NotificationHelper.showDailyQuestNotification(context)

                // Reschedule for next day
                CoroutineScope(Dispatchers.IO).launch {
                    val db = AppDatabase.getDatabase(context)
                    val user = db.userDao().getUser()
                    if (user != null && user.alarmEnabled) {
                        AlarmScheduler.scheduleDailyQuestAlarm(
                            context,
                            user.alarmHour,
                            user.alarmMinute
                        )
                    }
                }
            }

            AlarmScheduler.ACTION_PENALTY_WARNING -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val db = AppDatabase.getDatabase(context)
                    val user = db.userDao().getUser()
                    val quest = db.questDao().getDailyQuest()

                    // Only show penalty warning if daily quest is NOT completed
                    if (user != null && user.penaltyWarningEnabled && (quest == null || !quest.isCompleted)) {
                        NotificationHelper.showPenaltyWarningNotification(context)
                    }

                    // Reschedule for next evening
                    if (user != null && user.penaltyWarningEnabled) {
                        AlarmScheduler.schedulePenaltyWarning(context)
                    }
                }
            }
        }
    }
}
