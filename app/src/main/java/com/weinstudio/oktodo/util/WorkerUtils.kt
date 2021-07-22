package com.weinstudio.oktodo.util

import com.weinstudio.oktodo.worker.DailyNotificationsWorker
import java.util.*

object WorkerUtils {

    fun getNotificationInitialDelay(): Long {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, DailyNotificationsWorker.NOTIFY_HOUR)
        dueDate.set(Calendar.MINUTE, DailyNotificationsWorker.NOTIFY_MINUTE)

        dueDate.set(Calendar.SECOND, 0)
        dueDate.set(Calendar.MILLISECOND, 0)

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        return dueDate.timeInMillis - currentDate.timeInMillis
    }
}