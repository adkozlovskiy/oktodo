package com.weinstudio.memoria.service

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class QueryWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        return Result.success()
    }
}