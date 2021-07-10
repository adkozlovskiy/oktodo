package com.weinstudio.oktodo.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.weinstudio.oktodo.R
import com.weinstudio.oktodo.ui.splash.SplashActivity
import com.weinstudio.oktodo.util.WorkerUtil

class SyncWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object {

        const val WORK_TAG = "sync_work"

        const val NOTIFICATION_ID = 10003
        const val CHANNEL_ID = "periodically_sync"
        const val PREFERENCES_KEY = "periodically_sync"

        const val WORK_DELAY_HOURS = 8L
    }

    override suspend fun doWork(): Result {
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        val isEnabled = settings.getBoolean(PREFERENCES_KEY, true)

        if (isEnabled) {
            createNotificationChannel()
            setForeground(ForegroundInfo(NOTIFICATION_ID, getNotification()))

            // Sync problems with remote
            WorkerUtil.enqueueQueryWork(context, QueryWorker.QUERY_TYPE_REFRESH, null)

            // Next iteration
            WorkerUtil.enqueueSyncWork(context)
            return Result.success()
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