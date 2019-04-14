package com.base.hamoud.chronictrack.ui.main

import android.app.Application
import com.base.hamoud.chronictrack.BaseAndroidViewModel

class MainViewModel(application: Application) : BaseAndroidViewModel(application) {

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}