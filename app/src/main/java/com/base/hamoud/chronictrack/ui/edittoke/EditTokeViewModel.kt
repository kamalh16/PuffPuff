package com.base.hamoud.chronictrack.ui.edittoke

import android.app.Application
import android.text.format.DateFormat
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.repository.TokeRepo
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.*

class EditTokeViewModel(private var app: Application) : BaseAndroidViewModel(app) {

    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    lateinit var typeSelection: String
    lateinit var toolSelection: String
    lateinit var strainSelection: String

    var now: Calendar = Calendar.getInstance()

    var dateTimeLiveData: MutableLiveData<Calendar> = MutableLiveData()
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
                now.timeInMillis = it.tokeDateTime
                dateTimeLiveData.postValue(now)
                tokeForEditLive.postValue(it)
            }
        }
    }

    fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        now.set(
            year,
            month - 1,
            dayOfMonth,
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            now.get(Calendar.SECOND)
        )
        dateTimeLiveData.postValue(now)
    }

    fun updateTime(hour: Int, minute: Int) {
        now.set(
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DATE),
            hour,
            minute
        )
        dateTimeLiveData.postValue(now)
    }

    /**
     * @return [String] formatted date
     */
    fun getFormattedTokeDate(): String {
        return "${now.get(Calendar.DAY_OF_MONTH)}/${now.get(Calendar.MONTH)}/${now.get(Calendar.YEAR)}"
    }

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
        val spf = SimpleDateFormat(pattern, Locale.getDefault())
        return spf.format(now.time)
    }
}