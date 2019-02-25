package com.base.hamoud.chronictrack.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.HitRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : BaseAndroidViewModel(application) {

    var userRepo: UserRepo = UserRepo(db.userDao())
    var hitRepo: HitRepo = HitRepo(db.hitDao())

    var loggedInUserLive: MutableLiveData<User> = MutableLiveData()
    var userHitsCount: MutableLiveData<Int> = MutableLiveData()

    init {
        getLoggedInUser()
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun refreshHitsTotalCount() {
        ioScope.launch {
            val hitCount = hitRepo.getAllHits().count()
            userHitsCount.postValue(hitCount)
        }
    }

    private fun getLoggedInUser() {
        ioScope.launch(Dispatchers.IO) {
            loggedInUserLive.postValue(
                  userRepo.getUserByUsername("Chron")
            )
        }
    }

}