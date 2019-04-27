package com.base.hamoud.puffpuff.ui.edittoke

import android.app.Application
import android.text.format.DateFormat
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.puffpuff.BaseAndroidViewModel
import com.base.hamoud.puffpuff.data.entity.Toke
import com.base.hamoud.puffpuff.data.repository.TokeRepo
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.*

class EditTokeViewModel(private var app: Application) : BaseAndroidViewModel(app) {

    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    lateinit var typeSelection: String
    lateinit var toolSelection: String
    lateinit var strainSelection: String

    var tokeDateTime: DateTime = DateTime.now()

    var dateTimeLiveData: MutableLiveData<DateTime> = MutableLiveData()
    var tokeForEditLive: MutableLiveData<Toke> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun updateToke(toke: Toke) = ioScope.launch {
        tokeRepo.update(toke)
    }

    fun deleteToke(tokeId: String) = ioScope.launch {
        val tokeToDelete = tokeRepo.getTokeById(tokeId)
        tokeToDelete?.let {
            tokeRepo.deleteToke(it)
        }
    }

    fun getTokeForEdit(tokeId: String) {
        ioScope.launch {
            tokeRepo.getTokeById(tokeId)?.let {
                tokeDateTime = DateTime(it.tokeDateTime)
                dateTimeLiveData.postValue(tokeDateTime)
                tokeForEditLive.postValue(it)
            }
        }
    }

    fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        tokeDateTime = tokeDateTime.withYear(year)
            .withMonthOfYear(month)
            .withDayOfMonth(dayOfMonth)
            .withHourOfDay(tokeDateTime.hourOfDay)
            .withMinuteOfHour(tokeDateTime.minuteOfHour)
            .withSecondOfMinute(tokeDateTime.secondOfMinute)
        dateTimeLiveData.postValue(tokeDateTime)
    }

    fun updateTime(hour: Int, minute: Int) {
        tokeDateTime = tokeDateTime.withYear(tokeDateTime.year)
            .withMonthOfYear(tokeDateTime.monthOfYear)
            .withDayOfMonth(tokeDateTime.dayOfMonth)
            .withHourOfDay(hour)
            .withMinuteOfHour(minute)
        dateTimeLiveData.postValue(tokeDateTime)
    }

    /**
     * @return [String] formatted date
     */
    fun getFormattedTokeDate(): String {
        return "${tokeDateTime.dayOfMonth}/${tokeDateTime.monthOfYear}/${tokeDateTime.year}"
    }

    /**
     * Format [now] to a readable time decimalFormat based on the
     * device's decimalFormat (whether 24-hour decimalFormat is set in the device's system settings).
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
        val spf = SimpleDateFormat(pattern, Locale.getDefault())
        return spf.format(tokeDateTime.millis)
    }
}