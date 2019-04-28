package com.base.hamoud.puffpuff.ui.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.puffpuff.BaseAndroidViewModel
import com.base.hamoud.puffpuff.data.repository.SharedPrefsRepo

class MainViewModel(val app: Application) : BaseAndroidViewModel(app) {

    val saveStartupPageLive: MutableLiveData<String> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun getSavedStartupPage() = run {
        saveStartupPageLive.postValue(SharedPrefsRepo(app).getCurrentStartupPage())
    }
}