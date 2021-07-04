package com.weinstudio.memoria

import android.app.Application
import com.weinstudio.memoria.data.api.RetrofitBuilder
import com.weinstudio.memoria.data.db.ProblemDatabase
import com.weinstudio.memoria.data.repository.ProblemRepository

class MemoriaApplication : Application() {

    private val remoteSource = RetrofitBuilder.retrofitServices
    private val localSource by lazy { ProblemDatabase.getDatabase(this) }

    val repository by lazy {
        ProblemRepository(remoteSource, localSource.problemsDao())
    }
}