package com.base.hamoud.chronictrack.ui.addtoke

import android.app.Application
import android.text.format.DateFormat
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.repository.TokeRepo
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class AddTokeViewModel(private var app: Application) : BaseAndroidViewModel(app) {

    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    lateinit var typeSelection: String
    lateinit var toolSelection: String
    lateinit var strainSelection: String

    var now: OffsetDateTime

    var dateTimeLiveData: MutableLiveData<OffsetDateTime> = MutableLiveData()
    var lastAddedTokeLive: MutableLiveData<Toke> = MutableLiveData()

    init {
        now = OffsetDateTime.now()
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun saveToke() = ioScope.launch {
        val toke = Toke(
              tokeType = typeSelection,
              strain = strainSelection,
              tokeDateTime = now,
              toolUsed = toolSelection
        )
        tokeRepo.insert(toke)
    }

    fun getLastAddedToke() {
        ioScope.launch {
            tokeRepo.getLastTokeTaken().let {
                lastAddedTokeLive.postValue(it)
            }
        }
    }

    fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        now = now.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth)
        dateTimeLiveData.postValue(now)
    }

    fun updateTime(hour: Int, minute: Int) {
        now = now.withHour(hour).withMinute(minute)
        dateTimeLiveData.postValue(now)
    }

    /**
     * @return [String] formatted date
     */
    fun getFormattedTokeDate() = "${now.dayOfMonth} / ${now.monthValue} / ${now.year}"

    /**
     * Format [now] to a readable time format based on the
     * device's format (whether 24-hour format is set in the device's system settings).
     *
     * @return formatted [OffsetDateTime] as [String]
     */
    fun getFormattedTokeTime(): String {
        val isDevice24HourClock = DateFormat.is24HourFormat(app.applicationContext)
        val pattern = if (isDevice24HourClock) {
            "H:mm a"
        } else {
            "h:mm a"
        }
        // apply pattern and return
        return DateTimeFormatter
              .ofPattern(pattern)
              .format(now)
              .replace("AM", "am")
              .replace("PM", "pm")
    }

}