package com.base.hamoud.chronictrack.ui.journal

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.repository.TokeRepo
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class JournalViewModel(application: Application) : BaseAndroidViewModel(application) {

    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    var tokeListLive: MutableLiveData<MutableList<Toke>> = MutableLiveData()
    var todayTokesDataLive: MutableLiveData<List<Entry>?> = MutableLiveData()

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
                // we only want to run the lastTokedAt timer if the current day
                // is the same as the lastTokedAtTime day
                if (lastTokedAtTime >= journalDate.millis) { // FIXME
                    lastTokedAtTimeLive.postValue(null)
                } else {
                    val difference = now - lastTokedAtTime
                    lastTokedAtTimeLive.postValue(SystemClock.elapsedRealtime() - difference)
                }
            }
        }
    }

    private fun dateCompareDateTime(date: String): Int { // FIXME
        return SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US).format(date).compareTo(Date().toString())
    }

}