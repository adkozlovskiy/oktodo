package com.weinstudio.memoria

import android.app.Application
import com.weinstudio.memoria.data.api.RetrofitClient
import com.weinstudio.memoria.data.db.ProblemsDatabase
import com.weinstudio.memoria.data.repository.ProblemsRepository

class MemoriaApp : Application() {

    private val remoteSource = RetrofitClient.retrofitServices
    private val localSource by lazy { ProblemsDatabase.getDatabase(this) }

    val repository by lazy {
        ProblemsRepository(remoteSource, localSource.problemsDao())
    }
}