package com.base.hamoud.chronictrack.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.TokeRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import kotlin.random.Random

class HomeViewModel(application: Application) : BaseAndroidViewModel(application) {

    var userRepo: UserRepo = UserRepo(db.userDao())
    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    var loggedInUserLive: MutableLiveData<User> = MutableLiveData()
    var userTokesCountLive: MutableLiveData<Int> = MutableLiveData()

    init {
        // todo remove when done testing dates
//        ioScope.launch {
//            for ( i in 1..10) {
//                val toke = Toke(
//                    tokeDate = OffsetDateTime.now().minusDays((1L..3L).shuffled().first())
//                )
//
//                tokeRepo.insert(toke)
//            }
//        }
        getLoggedInUser()
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun refreshTokesTotalCount() {
        ioScope.launch {
            val hitCount = tokeRepo.getTodaysTokes().count()
            userTokesCountLive.postValue(hitCount)
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