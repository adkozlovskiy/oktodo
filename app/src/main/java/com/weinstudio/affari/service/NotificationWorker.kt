package com.weinstudio.affari.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.work.*
import com.weinstudio.affari.R
import com.weinstudio.affari.ui.splash.SplashActivity
import com.weinstudio.affari.util.WorkerUtil
import java.util.concurrent.TimeUnit

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val context = ctx

    override fun doWork(): Result {
        val settings = getDefaultSharedPreferences(context)
        val isEnabled = settings.getBoolean("notify_not_done", false)
        if (isEnabled) {
            val timeDiff = WorkerUtil.getWorkerInitialDelay()
            val dailyWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .addTag(WorkerUtil.WORK_TAG)
                .build()

            WorkManager.getInstance(applicationContext)
                .enqueueUniqueWork(
                    WorkerUtil.WORK_TAG,
                    ExistingWorkPolicy.REPLACE,
                    dailyWorkRequest
                )

            showNotification()
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
            PendingIntent.getActivity(context, 0, intent, 0)

        createNotificationChannel()

        val title = context.getString(R.string.notification_title)
        val text = context.getString(R.string.notification_content)

        val builder = NotificationCompat.Builder(context, WorkerUtil.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("$title — 5")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(WorkerUtil.NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.daily_notifications)
            val desc = context.getString(R.string.daily_notifications_desc)

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(WorkerUtil.CHANNEL_ID, name, importance)
                .apply {
                    description = desc
                }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}