package com.base.hamoud.puffpuff.ui.addtoke

import android.app.Application
import com.base.hamoud.puffpuff.BaseAndroidViewModel
import com.base.hamoud.puffpuff.data.entity.Toke
import com.base.hamoud.puffpuff.data.repository.TokeRepo
import kotlinx.coroutines.launch

class AddTokesViewModel(private var app: Application) : BaseAndroidViewModel(app) {

    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun getAllTokes(): MutableList<Toke> {
        var allTokes = mutableListOf<Toke>()
        ioScope.launch {
            allTokes = tokeRepo.getAllTokes()
        }
        return allTokes
    }
}