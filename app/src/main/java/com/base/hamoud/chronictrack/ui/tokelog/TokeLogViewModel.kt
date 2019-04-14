package com.base.hamoud.chronictrack.ui.tokelog

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.TokeRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.OffsetDateTime

class TokeLogViewModel(application: Application) : BaseAndroidViewModel(application) {

    var userRepo: UserRepo = UserRepo(db.userDao())
    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    var loggedInUserLive: MutableLiveData<User> = MutableLiveData()
    var userTokeListLive: MutableLiveData<List<Toke>> = MutableLiveData()
    var todayTokesDataLive: MutableLiveData<List<Entry>?> = MutableLiveData()

    var userTokesCountLive: MutableLiveData<Int> = MutableLiveData()
    var userLastTokeTodayLive: MutableLiveData<Long> = MutableLiveData()

    init {
        getLoggedInUser()
    }

    fun getTodaysTokesData() {
        ioScope.launch {
            val todaysTokes = tokeRepo.getTodaysTokes()

            todaysTokes.let { tokes ->
                val entries = ArrayList<Entry>(tokes.size)
                val twentyFourHrArr = IntArray(24) { 0 }
                for (toke in tokes) {
                    // get tokeCount in hour
                    twentyFourHrArr[toke.tokeDateTime.hour]++
                }

                for ((count, hour) in twentyFourHrArr.withIndex()) {
                    entries.add(Entry(count.toFloat(), hour.toFloat()))
                }
                entries.sortBy {
                    it.x
                }
                todayTokesDataLive.postValue(entries)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun refreshTokeList() {
        ioScope.launch {
            val tokes = tokeRepo.getTodaysTokes()
            userTokeListLive.postValue(tokes)
        }
    }

    fun refreshTokesTotalCount() {
        ioScope.launch {
            val todaysTokes = tokeRepo.getTodaysTokes()
            val hitCount = todaysTokes.count()
            userTokesCountLive.postValue(hitCount)

            if (hitCount > 0) {
                val lastTokeDateTime = todaysTokes[0].tokeDateTime
                val now = OffsetDateTime.now()
                val difference = Duration.between(lastTokeDateTime, now)
                userLastTokeTodayLive.postValue(SystemClock.elapsedRealtime() - difference.toMillis())
            }

        }
    }

    private fun getLoggedInUser() {
        ioScope.launch {
            loggedInUserLive.postValue(
                userRepo.getUserByUsername("Chron")
            )
        }
    }

}