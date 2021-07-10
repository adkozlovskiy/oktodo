package com.weinstudio.memoria.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.weinstudio.memoria.service.QueryWorker
import com.weinstudio.memoria.service.SyncWorker
import com.weinstudio.memoria.service.UnfulfilledWorker
import com.weinstudio.memoria.ui.main.MainActivity
import com.weinstudio.memoria.util.WorkerUtil

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sync problems with remote
        WorkerUtil.enqueueQueryWork(this, QueryWorker.QUERY_TYPE_REFRESH, null)

        val settings = PreferenceManager.getDefaultSharedPreferences(this)

        // Daily notifications
        val dailyNotificationsEnabled =
            settings.getBoolean(UnfulfilledWorker.PREFERENCES_KEY, true)

        if (dailyNotificationsEnabled) {
            WorkerUtil.enqueueNotificationWork(this)
        }

        // Periodically sync
        val periodicallySyncEnabled = settings.getBoolean(SyncWorker.PREFERENCES_KEY, true)

        if (periodicallySyncEnabled) {
            WorkerUtil.enqueueSyncWork(this)
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}