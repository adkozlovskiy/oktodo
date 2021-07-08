package com.weinstudio.memoria.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.weinstudio.memoria.MemoriaApp
import com.weinstudio.memoria.R
import com.weinstudio.memoria.ui.splash.SplashActivity
import com.weinstudio.memoria.util.WorkerUtil

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    companion object {

        const val WORK_TAG = "daily_work"

        const val NOTIFICATION_ID = 10002
        const val CHANNEL_ID = "daily_notifications"
        const val PREFERENCES_KEY = "notify_upcoming"

        const val NOTIFY_HOUR = 10
        const val NOTIFY_MINUTE = 0
    }

    private val context = ctx

    override fun doWork(): Result {
        val settings = getDefaultSharedPreferences(context)
        val isEnabled = settings.getBoolean(PREFERENCES_KEY, true)

        if (isEnabled) {
            showNotification()

            WorkerUtil.enqueueNotificationWork(context)
            return Result.success()
        }

        return Result.failure()
    }

    private fun showNotification() {
        val intent = Intent(context, SplashActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        createNotificationChannel()

        val title = context.getString(R.string.notification_title)
        val text = context.getString(R.string.notification_content)

        val countUnfulfilled =
            (applicationContext as MemoriaApp).repository.getCount(false)

        if (countUnfulfilled < 1) {
            return
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("$title — $countUnfulfilled")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.daily_notifications)
            val desc = context.getString(R.string.daily_notifications_desc)

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
                .apply {
                    description = desc
                }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}