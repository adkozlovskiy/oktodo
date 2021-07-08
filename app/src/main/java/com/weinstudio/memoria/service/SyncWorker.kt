package com.weinstudio.memoria.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.weinstudio.memoria.MemoriaApp
import com.weinstudio.memoria.R
import com.weinstudio.memoria.util.WorkerUtil

class SyncWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object {

        const val WORK_TAG = "sync_work"

        const val NOTIFICATION_ID = 10003
        const val CHANNEL_ID = "periodically_sync"
        const val PREFERENCES_KEY = "periodically_sync"

        const val WORK_DELAY_HOURS = 8L
    }

    private val repository = (context.applicationContext as MemoriaApp).repository

    override suspend fun doWork(): Result {
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        val isEnabled = settings.getBoolean(PREFERENCES_KEY, true)

        if (isEnabled) {
            setForeground(ForegroundInfo(NOTIFICATION_ID, getNotification()))

            repository.refreshProblems()
            WorkerUtil.enqueueSyncWork(context)
            return Result.success()
        }

        return Result.failure()
    }

    private fun getNotification(): Notification {
        createNotificationChannel()

        val cancelIntent = WorkManager.getInstance(context)
            .createCancelPendingIntent(id)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.sync_title))
            .setContentText(context.getString(R.string.sync_text))
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .setContentIntent(cancelIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.sync_channel_name)

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NotificationWorker.CHANNEL_ID, name, importance)

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}