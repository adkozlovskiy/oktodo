package com.weinstudio.affari

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.weinstudio.affari.service.NotificationWorker
import com.weinstudio.affari.util.WorkerUtil
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val settings = getDefaultSharedPreferences(this)

        // Settings application theme.
        val theme = settings.getString("theme", "light")
        if (theme == "light") {
            setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        } else {
            setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        // Periodically notifications
        val isEnabled = settings.getBoolean("notify_not_done", false)
        if (isEnabled) {
            configureNotificationWorker()
        }
    }

    private fun configureNotificationWorker() {
        val timeDiff = WorkerUtil.getWorkerInitialDelay()

        val dailyWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag(WorkerUtil.WORK_TAG)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniqueWork(WorkerUtil.WORK_TAG, ExistingWorkPolicy.REPLACE, dailyWorkRequest)
    }
}