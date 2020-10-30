package com.example.naturzaragoza.ui;

import android.content.Context
import android.content.SharedPreferences

class Settings(context: Context) {

    companion object {
        const val PREFS_NAME = "PrefsFile"
        const val PREF_FAMILY = "family"
        const val PREF_DEFAULT_FAMILY: String = "TODAS"

        fun instance(context: Context) = Settings(context)
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var family
        get() = preferences.getString(PREF_FAMILY, PREF_DEFAULT_FAMILY)
        set(value) {
            val editor = preferences.edit()
            editor.putString(PREF_FAMILY, value)
            editor.apply()
        }
}

