package com.base.hamoud.chronictrack.ui.tokelog

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.repository.TokeRepo
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.*
import kotlin.collections.ArrayList

class TokeLogViewModel(application: Application) : BaseAndroidViewModel(application) {

    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    var tokeListLive: MutableLiveData<List<Toke>> = MutableLiveData()
    var todayTokesDataLive: MutableLiveData<List<Entry>?> = MutableLiveData()

    var totalTokeCountLive: MutableLiveData<Int> = MutableLiveData()
    var lastTokedAtTimeLive: MutableLiveData<Long> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

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

    fun refreshTokeList() {
        ioScope.launch {
            val tokes = tokeRepo.getTodaysTokes()
            tokeListLive.postValue(tokes)
        }
    }

    fun refreshTokesTotalCount() {
        ioScope.launch {
            val todaysTokes = tokeRepo.getTodaysTokes()
            totalTokeCountLive.postValue(todaysTokes.count())
        }
    }

    fun refreshLastTokedAtTime() {
        ioScope.launch {
            val lastTokedAtTime = tokeRepo.getLastTokedAtTime()
            val now = DateTime.now().millis
            // determine time since last toked at
            // and post the result to lastTokedAtTimeLive
            lastTokedAtTime?.let {
                val difference = now - it
                lastTokedAtTimeLive.postValue(SystemClock.elapsedRealtime() - difference)
            }
        }
    }

}