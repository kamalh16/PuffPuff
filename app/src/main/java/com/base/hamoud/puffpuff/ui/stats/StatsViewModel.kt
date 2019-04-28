package com.base.hamoud.puffpuff.ui.stats

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.puffpuff.BaseAndroidViewModel
import com.base.hamoud.puffpuff.data.repository.TokeRepo
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class StatsViewModel(application: Application) : BaseAndroidViewModel(application) {

    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    var weeksTokesData: MutableLiveData<List<BarEntry>?> = MutableLiveData()
    var weeksTokesCount: MutableLiveData<Int> = MutableLiveData()

    init {
//        createFakeHitsWithRandomDates(20)
//        createFakeHitsWithRandomHours(10)
    }

    private fun createFakeHitsWithRandomDates(fakeHits: Int) {
//        ioScope.launch {
//            if (tokeRepo.getTokesFor(DateTime.now()).count() == 0) {
//                for (i in 1..fakeHits) {
////                    val toke = Toke(
////                        tokeDateTime = OffsetDateTime.tokeDateTime().minusDays((1L..3L).shuffled().first())
////                    )
//                    val tokeDateTime = Calendar.getInstance().timeInMillis - ((8.64*10000000*(1L..4L).shuffled().first()))
//                    val toke = Toke(tokeDateTime = tokeDateTime.toLong())
//
//                    tokeRepo.insert(toke)
//                }
//            }
//
//        }
    }

    private fun createFakeHitsWithRandomHours(fakeHits: Int) {
//        ioScope.launch {
//            if (tokeRepo.getTokesFor(DateTime.now()).count() == 0) {
//                for (i in 1..fakeHits) {
////                    val time = OffsetDateTime.tokeDateTime()
////                        .minusHours((0L..10L).shuffled().first())
////                        .minusDays((0L..4L).shuffled().first())
//
//                    val time = Calendar.getInstance().timeInMillis
//                    Timber.i("time: $time")
////                    val toke = Toke(
////                        tokeDateTime = time.plusMinutes((1L..39L).shuffled().first())
////                    )
//                    val toke = Toke(tokeDateTime = time)
//                    tokeRepo.insert(toke)
//                }
//            }
//        }
    }

    fun getThisWeeksTokesData() {
        ioScope.launch {
            val weeksTokes = tokeRepo.getThisWeeksTokes()
            weeksTokes.let {
                var entries = ArrayList<BarEntry>(it.size)
                val weeksArr = IntArray(7) { 0 }
                for (toke in it) {
                    // get tokeCount in hour
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = toke.tokeDateTime
                    weeksArr[cal.get(Calendar.DAY_OF_WEEK) - 1]++
                }
                var weeklyCount = 0
                for ((day, count) in weeksArr.withIndex()) {
                    weeklyCount += count
                    entries.add(BarEntry(day.toFloat(), count.toFloat()))
                }

                entries.sortBy { elem ->
                    elem.x
                }

                weeksTokesData.postValue(entries)
                weeksTokesCount.postValue(weeklyCount)
            }
        }
    }

    private fun moveFirstFourHoursToEnd(entries: ArrayList<BarEntry>): ArrayList<BarEntry> {
        var x = 0
        var tempEntries = entries
        while (x < 4) {
            tempEntries = moveFirstElementToLast(tempEntries)
            x++
        }
        return tempEntries
    }

    private fun moveFirstElementToLast(entries: ArrayList<BarEntry>): ArrayList<BarEntry> {
        val temp = entries.removeAt(0)
        entries.add(temp)
        return entries
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}