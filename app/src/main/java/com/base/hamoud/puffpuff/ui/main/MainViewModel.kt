package com.base.hamoud.puffpuff.ui.main

import android.app.Application
import com.base.hamoud.puffpuff.BaseAndroidViewModel

class MainViewModel(application: Application) : BaseAndroidViewModel(application) {

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}