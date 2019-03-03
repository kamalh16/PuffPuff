package com.base.hamoud.chronictrack.ui.home

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.TokeRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Duration
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit

class HomeViewModel(application: Application) : BaseAndroidViewModel(application) {

    var userRepo: UserRepo = UserRepo(db.userDao())
    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    var loggedInUserLive: MutableLiveData<User> = MutableLiveData()
    var userTokesCountLive: MutableLiveData<Int> = MutableLiveData()
    var userLastTokeTodayLive: MutableLiveData<Long> = MutableLiveData()

    init {
//        createFakeHitsWithRandomDates(10)

        getLoggedInUser()
    }

    private fun createFakeHitsWithRandomDates(fakeHits: Int) {
        ioScope.launch {
            for (i in 1..fakeHits) {
                val toke = Toke(
                    tokeDateTime = OffsetDateTime.now().minusDays((1L..3L).shuffled().first())
                )

                tokeRepo.insert(toke)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun refreshTokesTotalCount() {
        ioScope.launch {
            val todaysTokes = tokeRepo.getTodaysTokes()
            val hitCount = todaysTokes.count()
            userTokesCountLive.postValue(hitCount)

            if (hitCount > 0) {
                val lastTokeDateTime = todaysTokes[0].tokeDateTime
                val now = OffsetDateTime.now()
                val difference =  Duration.between(lastTokeDateTime, now)
                userLastTokeTodayLive.postValue(SystemClock.elapsedRealtime() - difference.toMillis())
            }

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