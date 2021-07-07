package com.weinstudio.memoria.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class SyncWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    companion object {
        const val WORK_TAG = "sync_work"
    }

    override suspend fun doWork(): Result {
        return Result.success()
    }
}