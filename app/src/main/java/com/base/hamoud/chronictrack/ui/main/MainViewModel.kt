package com.base.hamoud.chronictrack.ui.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Hit
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.HitRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MainViewModel(application: Application) : BaseAndroidViewModel(application) {

    // set logged in user
    private var user: User = User(UUID.randomUUID().toString(), "Chron")

    var userRepo: UserRepo = UserRepo(db.userDao())
    var hitRepo: HitRepo = HitRepo(db.hitDao())

    var loggedInUserLive: MutableLiveData<User> = MutableLiveData()
    var userHitsListLive: MutableLiveData<MutableList<Hit>> = MutableLiveData()
    var userHitsCount: MutableLiveData<Int> = MutableLiveData()

    init {
        attemptInsertUser(user)
        Timber.i("attemptInsertUser: $user")
    }

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

    private fun attemptInsertUser(user: User) {
        ioScope.launch(Dispatchers.IO) {
            loggedInUserLive.postValue(
                  userRepo.insert(user)
            )
        }
    }

}