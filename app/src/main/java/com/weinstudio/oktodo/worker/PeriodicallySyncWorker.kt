package com.weinstudio.oktodo.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.preference.PreferenceManager
import androidx.work.*
import com.weinstudio.oktodo.R
import com.weinstudio.oktodo.data.ProblemsRepository
import com.weinstudio.oktodo.ui.splash.SplashActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class PeriodicallySyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val repository: ProblemsRepository
) :
    CoroutineWorker(context, params) {

    companion object {

        const val WORK_TAG = "sync_work"

        const val NOTIFICATION_ID = 10003
        const val CHANNEL_ID = "periodically_sync"
        const val PREFERENCES_KEY = "periodically_sync"

        const val WORK_DELAY_HOURS = 8L

        fun enqueuePeriodicallySync(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val syncWorkRequest = OneTimeWorkRequestBuilder<PeriodicallySyncWorker>()
                .setInitialDelay(WORK_DELAY_HOURS, TimeUnit.HOURS)
                .setConstraints(constraints)
                .addTag(WORK_TAG)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(WORK_TAG, ExistingWorkPolicy.KEEP, syncWorkRequest)
        }
    }

    override suspend fun doWork(): Result {
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        val enabled = settings.getBoolean(PREFERENCES_KEY, true)

        if (enabled) {
            createNotificationChannel()
            setForeground(ForegroundInfo(NOTIFICATION_ID, getNotification()))

            enqueuePeriodicallySync(context)
            return repository.refreshProblems()
        }

        return Result.failure()
    }

    private fun getNotification(): Notification {
        val pendingIntent: PendingIntent =
            Intent(context, SplashActivity::class.java).let {
                PendingIntent.getActivity(
                    context, 0, it, PendingIntent.FLAG_IMMUTABLE
                )
            }

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setOngoing(true)
            .setContentTitle(context.getString(R.string.sync_title))
            .setContentText(context.getString(R.string.sync_text))
            .setSmallIcon(R.drawable.ic_sync)
            .setContentIntent(pendingIntent)
            .setShowWhen(false)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.sync_channel_name)

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}