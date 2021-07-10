package com.weinstudio.memoria

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.weinstudio.memoria.data.db.ProblemsDatabase
import com.weinstudio.memoria.data.repository.ProblemsRepository

class MemoriaApp : Application() {

    private val localSource by lazy { ProblemsDatabase.getDatabase(this) }

    val repository by lazy {
        ProblemsRepository(localSource.problemsDao(), this)
    }

    override fun onCreate() {
        super.onCreate()
        val settings = PreferenceManager.getDefaultSharedPreferences(this)

        // Settings application theme
        val theme = settings.getString("theme", "light")
        if (theme == "light") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}