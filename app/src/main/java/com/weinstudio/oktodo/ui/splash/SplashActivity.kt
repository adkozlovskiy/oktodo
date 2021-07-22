package com.weinstudio.oktodo.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.weinstudio.oktodo.ui.main.view.MainActivity
import com.weinstudio.oktodo.worker.DailyNotificationsWorker
import com.weinstudio.oktodo.worker.PeriodicallySyncWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settings = getDefaultSharedPreferences(this)
        val themeValue = settings.getString("theme", "light")!!

        setApplicationTheme(themeValue)

        val dailyNotificationsEnabled =
            settings.getBoolean(DailyNotificationsWorker.PREFERENCES_KEY, true)

        val periodicallySyncEnabled =
            settings.getBoolean(PeriodicallySyncWorker.PREFERENCES_KEY, true)

        with(viewModel) {
            enqueueRefreshProblems()

            if (dailyNotificationsEnabled) {
                enqueueDailyNotifications()
            }

            if (periodicallySyncEnabled) {
                enqueuePeriodicallySync()
            }
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setApplicationTheme(themeValue: String) {
        AppCompatDelegate.setDefaultNightMode(
            if (themeValue == "light") {
                AppCompatDelegate.MODE_NIGHT_NO

            } else AppCompatDelegate.MODE_NIGHT_YES
        )
    }
}