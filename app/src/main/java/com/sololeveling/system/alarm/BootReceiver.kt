package com.sololeveling.system.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sololeveling.system.data.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(context)
                val user = db.userDao().getUser() ?: return@launch

                if (user.alarmEnabled) {
                    AlarmScheduler.scheduleDailyQuestAlarm(
                        context,
                        user.alarmHour,
                        user.alarmMinute
                    )
                }

                if (user.penaltyWarningEnabled) {
                    AlarmScheduler.schedulePenaltyWarning(context)
                }
            }
        }
    }
}
