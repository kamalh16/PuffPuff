package com.base.hamoud.chronictrack.ui.log

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Hit
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.HitRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TokeLogViewModel(application: Application) : BaseAndroidViewModel(application) {

    var userRepo: UserRepo = UserRepo(db.userDao())
    var hitRepo: HitRepo = HitRepo(db.hitDao())

    var loggedInUserLive: MutableLiveData<User> = MutableLiveData()
    var userHitsListLive: MutableLiveData<MutableList<Hit>> = MutableLiveData()
    var userHitsCount: MutableLiveData<Int> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun refreshHitsList() {
        ioScope.launch {
            val hits = hitRepo.getAllHits()
            userHitsListLive.postValue(hits)
        }
    }

    fun refreshHitsTotalCount() {
        ioScope.launch {
            val hitCount = hitRepo.getAllHits().count()
            userHitsCount.postValue(hitCount)
        }
    }

    fun insertHit(hit: Hit) = ioScope.launch {
        hitRepo.insert(hit)
        refreshHitsTotalCount()
    }

    fun deleteHit(hit: Hit) = ioScope.launch {
        hitRepo.deleteHit(hit)
        refreshHitsTotalCount()
    }

    private fun getLoggedInUser() {
        ioScope.launch(Dispatchers.IO) {
            loggedInUserLive.postValue(
                  userRepo.getUserByUsername("Chron")
            )
        }
    }

}