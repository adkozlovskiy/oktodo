package com.weinstudio.oktodo.ui.splash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.weinstudio.oktodo.ui.main.MainActivity
import com.weinstudio.oktodo.util.WorkerEnquirer
import com.weinstudio.oktodo.worker.DailyNotificationsWorker
import com.weinstudio.oktodo.worker.PeriodicallySyncWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var workerEnquirer: WorkerEnquirer

    lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settings = getDefaultSharedPreferences(this)

        enqueueRefreshProblems()
        enqueuePeriodicallySync()
        enqueueDailyNotifications()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun enqueueRefreshProblems() {
        workerEnquirer.enqueueRefresh()
    }

    private fun enqueueDailyNotifications() {
        val enabled = settings.getBoolean(DailyNotificationsWorker.PREFERENCES_KEY, true)

        if (enabled) {
            workerEnquirer.enqueueDailyNotifications()
        }
    }

    private fun enqueuePeriodicallySync() {
        val enabled = settings.getBoolean(PeriodicallySyncWorker.PREFERENCES_KEY, true)

        if (enabled) {
            workerEnquirer.enqueuePeriodicallySync()
        }
    }
}