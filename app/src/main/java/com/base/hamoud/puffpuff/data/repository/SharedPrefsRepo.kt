package com.base.hamoud.puffpuff.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.base.hamoud.puffpuff.ui.settings.model.Settings

class SharedPrefsRepo(private val applicationContext: Context) {

    companion object {
        // prefs name
        private const val SHARED_PREFS = "prefs"

        //region SharedPrefs Options
        private const val PREF_THEME = "pref_theme"
        private const val PREF_NEXT_TOKE_REMINDER = "pref_next_toke_reminder"
        //endregion

        //region SharedPrefs Defaults
        private const val DEFAULT_THEME = Settings.Value.LIGHT_THEME
        private const val DEFAULT_NEXT_TOKE_REMINDER = -1L
        //endregion
    }

    private val sharedPref: SharedPreferences =
        applicationContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    fun getUserSettings(): Settings {
        return Settings().apply {
            this.theme = getThemeChoice()
            this.nextTokeReminderTime = getNextTokeReminderTime()
        }
    }

    fun saveThemeChoice(theme: String) {
        sharedPref.edit { putString(PREF_THEME, theme) }
    }

    private fun getThemeChoice(): String {
        return sharedPref.getString(PREF_THEME, DEFAULT_THEME)
    }

    fun saveNextTokeReminderTime(time: Long) {
        sharedPref.edit { putLong(PREF_NEXT_TOKE_REMINDER, time) }
    }

    private fun getNextTokeReminderTime(): Long {
        return sharedPref.getLong(PREF_NEXT_TOKE_REMINDER, DEFAULT_NEXT_TOKE_REMINDER)
    }
}