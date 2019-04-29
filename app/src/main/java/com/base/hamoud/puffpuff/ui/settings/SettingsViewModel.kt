package com.base.hamoud.puffpuff.ui.settings

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.puffpuff.BaseAndroidViewModel
import com.base.hamoud.puffpuff.data.repository.SharedPrefsRepo
import com.base.hamoud.puffpuff.data.repository.TokeRepo
import com.base.hamoud.puffpuff.ui.main.MainNavScreen
import com.base.hamoud.puffpuff.ui.settings.model.SettingsItem
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : BaseAndroidViewModel(application) {

    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())
    var applicationContext: Context = application.applicationContext
    val sharedPrefsRepo = SharedPrefsRepo(applicationContext)

    val userSettingsLive = MutableLiveData<SettingsItem>()

    val settingsItem: SettingsItem = SettingsItem

    init {
        settingsItem.optionStartupPage = sharedPrefsRepo.getCurrentStartupPage()!!
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun clearAllData() {
        ioScope.launch {
            tokeRepo.deleteAllTokes()
        }
    }

    fun saveStartupPage(page: String) {
        sharedPrefsRepo.saveStartupPage(page)
        Toast.makeText(applicationContext, "Startup Page: $page", Toast.LENGTH_LONG).show()
        retrieveCurrentStartupPage()
    }

    private fun retrieveCurrentStartupPage() {
        settingsItem.optionStartupPage = sharedPrefsRepo.getCurrentStartupPage()!!
        postUserSettings()
    }

    fun postUserSettings() {
        userSettingsLive.postValue(settingsItem)
    }
}