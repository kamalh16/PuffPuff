package com.base.hamoud.chronictrack.ui.stats

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.repository.TokeRepo
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class StatsViewModel(application: Application) : BaseAndroidViewModel(application) {

    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    var weeksTokesData: MutableLiveData<List<Entry>?> = MutableLiveData()

    init {
//        createFakeHitsWithRandomDates(20)
//        createFakeHitsWithRandomHours(10)
    }

    private fun createFakeHitsWithRandomDates(fakeHits: Int) {
        ioScope.launch {
            if (tokeRepo.getTodaysTokes().count() == 0) {
                for (i in 1..fakeHits) {
//                    val toke = Toke(
//                        tokeDateTime = OffsetDateTime.tokeDateTime().minusDays((1L..3L).shuffled().first())
//                    )
                    val tokeDateTime = Calendar.getInstance().timeInMillis - ((8.64*10000000*(1L..4L).shuffled().first()))
                    val toke = Toke(tokeDateTime = tokeDateTime.toLong())

                    tokeRepo.insert(toke)
                }
            }

        }
    }

    private fun createFakeHitsWithRandomHours(fakeHits: Int) {
        ioScope.launch {
            if (tokeRepo.getTodaysTokes().count() == 0) {
                for (i in 1..fakeHits) {
//                    val time = OffsetDateTime.tokeDateTime()
//                        .minusHours((0L..10L).shuffled().first())
//                        .minusDays((0L..4L).shuffled().first())

                    val time = Calendar.getInstance().timeInMillis
                    Timber.i("time: $time")
//                    val toke = Toke(
//                        tokeDateTime = time.plusMinutes((1L..39L).shuffled().first())
//                    )
                    val toke = Toke(tokeDateTime = time)
                    tokeRepo.insert(toke)
                }
            }
        }
    }

    fun getThisWeeksTokesData() {
        ioScope.launch {
            val weeksTokes = tokeRepo.getThisWeeksTokes() // todo
            weeksTokes.let {
                val entries = ArrayList<Entry>(it.size)
                val weeksArr = IntArray(7) { 0 }
                for (toke in it) {
                    // get tokeCount in hour
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = toke.tokeDateTime
                    weeksArr[cal.get(Calendar.DAY_OF_WEEK) - 1]++
                }

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

}