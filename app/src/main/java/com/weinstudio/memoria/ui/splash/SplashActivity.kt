package com.weinstudio.memoria.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.weinstudio.memoria.service.NotificationWorker
import com.weinstudio.memoria.ui.main.MainActivity
import com.weinstudio.memoria.util.WorkerUtil

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settings = PreferenceManager.getDefaultSharedPreferences(this)

        // Settings application theme.
        val theme = settings.getString("theme", "light")
        if (theme == "light") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        // Periodically notifications.
        val isEnabled = settings.getBoolean(NotificationWorker.PREFERENCES_KEY, false)
        if (isEnabled) {
            WorkerUtil.enqueueNotificationWork(this)
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}