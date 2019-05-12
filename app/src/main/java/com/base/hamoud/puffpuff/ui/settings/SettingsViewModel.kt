package com.base.hamoud.puffpuff.ui.settings

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.puffpuff.BaseAndroidViewModel
import com.base.hamoud.puffpuff.data.repository.SharedPrefsRepo
import com.base.hamoud.puffpuff.data.repository.TokeRepo
import com.base.hamoud.puffpuff.ui.settings.model.Settings
import kotlinx.coroutines.launch

class SettingsViewModel(val app: Application) : BaseAndroidViewModel(app) {

    private var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())
    private val sharedPrefsRepo = SharedPrefsRepo(app)

    val userSettingsLive = MutableLiveData<Settings>()
    val settingsRowListLive = MutableLiveData<ArrayList<String>>()

    init {
        getUserSettings()
        postSettingsRowList()
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

    fun getUserSettings() {
        val userSettings = sharedPrefsRepo.getUserSettings()
        userSettingsLive.postValue(userSettings)
    }

    fun saveSettings(settings: Settings) {
        sharedPrefsRepo.saveThemeChoice(settings.theme)
        sharedPrefsRepo.saveNextTokeReminderTime(settings.nextTokeReminderTime)
    }

    private fun postSettingsRowList() {
        settingsRowListLive.postValue(Settings.RowOption.list)
    }
}