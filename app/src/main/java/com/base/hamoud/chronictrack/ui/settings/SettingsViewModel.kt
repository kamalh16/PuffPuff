package com.base.hamoud.chronictrack.ui.settings

import android.app.Application
import com.base.hamoud.chronictrack.BaseAndroidViewModel

class SettingsViewModel(application: Application) : BaseAndroidViewModel(application) {

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}