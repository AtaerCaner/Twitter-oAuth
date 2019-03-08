package com.sahibinden.ataercanercelik.challenge.manager

import android.content.Context

object AppDataSharedPreferences {

    fun setSettingsValue(key: String, value: String, ctx: Context) {
        val prefsEditor = SharedPreferencesProvider.getAppDataSharedPreferences(ctx).edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun getStringSettingsValue(key: String, ctx: Context): String? {
        var value: String? = ""
        val mPrefs = SharedPreferencesProvider.getAppDataSharedPreferences(ctx)
        if (mPrefs != null) {
            value = mPrefs.getString(key, "")
        }
        return value
    }
}