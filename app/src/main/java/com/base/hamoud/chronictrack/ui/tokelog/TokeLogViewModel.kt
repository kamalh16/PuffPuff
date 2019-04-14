package com.base.hamoud.chronictrack.ui.tokelog

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.repository.TokeRepo
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class TokeLogViewModel(application: Application) : BaseAndroidViewModel(application) {

    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    var userTokeListLive: MutableLiveData<List<Toke>> = MutableLiveData()
    var todayTokesDataLive: MutableLiveData<List<Entry>?> = MutableLiveData()

    var userTokesCountLive: MutableLiveData<Int> = MutableLiveData()
    var userLastTokeTodayLive: MutableLiveData<Long> = MutableLiveData()

    fun getTodaysTokesData() {
        ioScope.launch {
            val todaysTokes = tokeRepo.getTodaysTokes()

            todaysTokes.let { tokes ->
                val entries = ArrayList<Entry>(tokes.size)
                val twentyFourHrArr = IntArray(24) { 0 }
                for (toke in tokes) {
                    // get tokeCount in hour
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = toke.tokeDateTime
                    twentyFourHrArr[cal.get(Calendar.HOUR)]++
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
                val now = Calendar.getInstance().timeInMillis
                val difference = now - lastTokeDateTime
                userLastTokeTodayLive.postValue(SystemClock.elapsedRealtime() - difference)
            }
        }
    }

}