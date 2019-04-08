package com.base.hamoud.chronictrack.ui.home

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.TokeRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Duration
import java.time.OffsetDateTime

class HomeViewModel(application: Application) : BaseAndroidViewModel(application) {

    var userRepo: UserRepo = UserRepo(db.userDao())
    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    var loggedInUserLive: MutableLiveData<User> = MutableLiveData()
    var userTokesCountLive: MutableLiveData<Int> = MutableLiveData()
    var userLastTokeTodayLive: MutableLiveData<Long> = MutableLiveData()
    var todayTokesData: MutableLiveData<List<Entry>?> = MutableLiveData()
    var weeksTokesData: MutableLiveData<List<Entry>?> = MutableLiveData()

    init {
//        createFakeHitsWithRandomDates(10)
        createFakeHitsWithRandomHours(20)

        getLoggedInUser()
    }

    private fun createFakeHitsWithRandomDates(fakeHits: Int) {
        ioScope.launch {
            if (tokeRepo.getTodaysTokes().count() == 0) {
                for (i in 1..fakeHits) {
                    val toke = Toke(
                        tokeDateTime = OffsetDateTime.now().minusDays((1L..3L).shuffled().first())
                    )

                    tokeRepo.insert(toke)
                }
            }

        }
    }

    private fun createFakeHitsWithRandomHours(fakeHits: Int) {
        ioScope.launch {
            if (tokeRepo.getTodaysTokes().count() == 0) {
                for (i in 1..fakeHits) {
                    val time = OffsetDateTime.now()
                        .minusHours((0L..10L).shuffled().first())
                        .minusDays((0L..4L).shuffled().first())
                    Timber.i("time: $time")
                    val toke = Toke(
                        tokeDateTime =time.plusMinutes((1L..39L).shuffled().first())
                    )
                    tokeRepo.insert(toke)
                }
            }
        }
    }

    fun getTodaysTokesData() {
        ioScope.launch {
            val todaysTokes = tokeRepo.getTodaysTokes()

            todaysTokes.let {
                val entries = ArrayList<Entry>(it.size)

                for (toke in todaysTokes) {
                    // get tokeCount in hour
                    val tempHour = toke.tokeDateTime.hour
                    val tempMin = toke.tokeDateTime.minute
                    entries.add(Entry(tempHour.toFloat(), tempMin.toFloat()))
                }
                entries.sortBy {
                    it.x
                }
                todayTokesData.postValue(entries)
            }
        }
    }

    fun getThisWeeksTokesData() {
        ioScope.launch {
            val weeksTokes = tokeRepo.getThisWeeksTokes() // todo
            weeksTokes.let {
                val entries = ArrayList<Entry>(it.size)
                val weeksArr = arrayOf(0,0,0,0,0,0,0,0)
                for (toke in it) {
                    // get tokeCount in hour
                    weeksArr[toke.tokeDateTime.dayOfWeek.value]++
                }
                val weekNames = arrayOf(" ","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

                for ((count, day) in weeksArr.withIndex()) {
                    entries.add(Entry(count.toFloat(), day.toFloat()))
                }

                entries.sortBy {
                    it.x
                }
                weeksTokesData.postValue(entries)
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