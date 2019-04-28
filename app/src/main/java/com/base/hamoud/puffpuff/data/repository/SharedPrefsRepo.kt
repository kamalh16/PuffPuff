package com.base.hamoud.puffpuff.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.core.content.edit
import com.base.hamoud.puffpuff.Constants.SHARED_PREFS
import com.base.hamoud.puffpuff.Constants.STARTUP_PAGE_PREFS
import com.base.hamoud.puffpuff.ui.main.MainNavScreen

class SharedPrefsRepo(private val applicationContext: Context ) {

    private val sharedPref: SharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    fun saveStartupPage(page: String) {
        sharedPref.edit {
            putString(STARTUP_PAGE_PREFS, page)
            commit()
        }

        Toast.makeText(applicationContext, "Set ${getPageText(page)} as Startup Page", Toast.LENGTH_SHORT).show()
    }

    private fun getPageText(page: String): String {
        return when (page) {
            MainNavScreen.STATS_SCREEN -> "Stats"
            else -> "Journal"
        }
    }


    fun getCurrentStartupPage(): String? {
        return sharedPref.getString(STARTUP_PAGE_PREFS, MainNavScreen.JOURNAL_SCREEN)
    }
}