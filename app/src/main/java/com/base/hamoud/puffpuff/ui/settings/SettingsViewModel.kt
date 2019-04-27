package com.base.hamoud.puffpuff.ui.settings

import android.app.Application
import com.base.hamoud.puffpuff.BaseAndroidViewModel
import com.base.hamoud.puffpuff.data.repository.TokeRepo
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : BaseAndroidViewModel(application) {

    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

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