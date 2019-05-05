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

    val settingsLive = MutableLiveData<Settings>()
    val settingsOptionsLive = MutableLiveData<ArrayList<String>>()

    init {
        postSettingsValue()
        postSettingsOptionsValue()
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

    fun saveSettings(settings: Settings) {
        saveTheme(settings.theme)
        saveNextTokeReminderTime(settings.nextTokeReminderTime)
    }

    private fun saveTheme(theme: String) {
        sharedPrefsRepo.saveThemeChoice(theme)
    }

    private fun saveNextTokeReminderTime(time: Long) {
        sharedPrefsRepo.saveNextTokeReminderTime(time)
    }

    private fun postSettingsValue() {
        settingsLive.postValue(
            Settings(
                theme = sharedPrefsRepo.getThemeChoice(),
                nextTokeReminderTime = sharedPrefsRepo.getNextTokeReminderTime()
            )
        )
    }

    private fun postSettingsOptionsValue() {
        settingsOptionsLive.postValue(Settings.RowOption.list)
    }
}