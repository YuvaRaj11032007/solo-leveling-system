package com.sololeveling.system

import android.app.Application
import com.sololeveling.system.alarm.NotificationHelper

class SystemApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannels(this)
    }
}
