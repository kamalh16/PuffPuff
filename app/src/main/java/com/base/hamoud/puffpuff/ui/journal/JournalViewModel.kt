package com.base.hamoud.puffpuff.ui.journal

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.puffpuff.BaseAndroidViewModel
import com.base.hamoud.puffpuff.data.entity.Toke
import com.base.hamoud.puffpuff.data.repository.TokeRepo
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.*
import kotlin.collections.ArrayList

class JournalViewModel(application: Application) : BaseAndroidViewModel(application) {

    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    var tokeListLive: MutableLiveData<MutableList<Toke>> = MutableLiveData()
    var todayTokesDataLive: MutableLiveData<List<BarEntry>?> = MutableLiveData()

    var totalTokeCountLive: MutableLiveData<Int> = MutableLiveData()
    var lastTokedAtTimeLive: MutableLiveData<Long> = MutableLiveData()
    var calendarMinDateTimeLive: MutableLiveData<Long> = MutableLiveData()

    var journalDateLive: MutableLiveData<DateTime> = MutableLiveData() // for ui
    var journalDate: DateTime = DateTime.now() // for this viewModel

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun updateJournalDate(dt: DateTime) {
        journalDate = dt
        journalDateLive.postValue(dt)
    }

    fun getTodaysTokesData() {
        ioScope.launch {
            val todaysTokes = tokeRepo.getTokesFor(journalDate)

            todaysTokes.let { tokes ->
                val entries = ArrayList<BarEntry>(tokes.size)
                val twentyFourHrArr = IntArray(24) { 0 }
                val cal = Calendar.getInstance()
                for (toke in tokes) {
                    // get tokeCount in hour
//                    cal.timeInMillis = toke.tokeDateTime
                    val tokeDatetime = DateTime(toke.tokeDateTime)
                    twentyFourHrArr[tokeDatetime.hourOfDay]++
                }

                for ((count, hour) in twentyFourHrArr.withIndex()) {
                    entries.add(BarEntry(count.toFloat(), hour.toFloat()))
                }
                entries.sortBy {
                    it.x
                }
                todayTokesDataLive.postValue(entries)
            }
        }
    }

    fun getFirstEverSavedToke() {
        ioScope.launch {
            val firstEverTokeDateTime = tokeRepo.getFirstEverSavedTokeDateTime()
            calendarMinDateTimeLive.postValue(firstEverTokeDateTime)
        }
    }

    fun refreshTokeList() {
        ioScope.launch {
            val tokes = tokeRepo.getTokesFor(journalDate)
            tokeListLive.postValue(tokes)
        }
    }

    fun refreshTokesTotalCount() {
        ioScope.launch {
            val todaysTokes = tokeRepo.getTokesFor(journalDate)
            totalTokeCountLive.postValue(todaysTokes.count())
        }
    }

    fun refreshLastTokedAtTime() {
        ioScope.launch {
            val now = DateTime.now().millis
            // determine time since last toked at
            // and post the result to lastTokedAtTimeLive
            tokeRepo.getLastTokedAtTime()?.let { lastTokedAtTime ->
                // We only want to run the lastTokedAt timer if it's today; so if the lastTokedAtTime
                // is before the selected journalDate. Otherwise, don't run the timer because the
                // lastTokedAtTime is after the selected journalDate which is in the past.
                if (DateTime(lastTokedAtTime).isBefore(journalDate)) {
                    val difference = now - lastTokedAtTime
                    lastTokedAtTimeLive.postValue(SystemClock.elapsedRealtime() - difference)
                } else {
                    lastTokedAtTimeLive.postValue(null)
                }
            }
        }
    }

}