package com.weinstudio.affari

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.preference.PreferenceManager.getDefaultSharedPreferences

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val settings = getDefaultSharedPreferences(this)

        val theme = settings.getString("theme", "light")

        if (theme == "light") {
            setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        } else {
            setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}