package com.base.hamoud.chronictrack.ui.settings

import android.app.Application
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.repository.HitRepo
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : BaseAndroidViewModel(application) {

    var hitRepo: HitRepo = HitRepo(db.hitDao())

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun clearAllData() {
        ioScope.launch {
            hitRepo.deleteAllTokes()
        }
    }

}