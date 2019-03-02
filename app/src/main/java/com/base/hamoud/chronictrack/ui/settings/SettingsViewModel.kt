package com.base.hamoud.chronictrack.ui.settings

import android.app.Application
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.repository.TokeRepo
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : BaseAndroidViewModel(application) {

    var tokeRepo: TokeRepo = TokeRepo(db.hitDao())

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun clearAllData() {
        ioScope.launch {
            tokeRepo.deleteAllTokes()
        }
    }

}