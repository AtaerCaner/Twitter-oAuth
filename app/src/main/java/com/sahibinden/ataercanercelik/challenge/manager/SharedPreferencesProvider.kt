package com.sahibinden.ataercanercelik.challenge.manager

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesProvider {
    private val APP_DATA_PREFERENCES = "AppDataPreferences"

    fun getAppDataSharedPreferences(c: Context): SharedPreferences {
        return c.getSharedPreferences(APP_DATA_PREFERENCES, Context.MODE_PRIVATE)
    }

}