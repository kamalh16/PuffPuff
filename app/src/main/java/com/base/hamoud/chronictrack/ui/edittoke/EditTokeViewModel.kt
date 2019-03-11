package com.base.hamoud.chronictrack.ui.edittoke

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.TokeRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

class EditTokeViewModel(application: Application) : BaseAndroidViewModel(application) {

    var userRepo: UserRepo = UserRepo(db.userDao())
    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    lateinit var typeSelection: String
    lateinit var methodSelection: String
    lateinit var strainSelection: String

    var now: OffsetDateTime

    var loggedInUserLive: MutableLiveData<User> = MutableLiveData()
    var dateTimeLiveData: MutableLiveData<OffsetDateTime> = MutableLiveData()
    var tokeForEditLive: MutableLiveData<Toke> = MutableLiveData()

    init {
        getLoggedInUser()
        now = OffsetDateTime.now()
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun updateToke(toke: Toke) = ioScope.launch {
        tokeRepo.update(toke)
    }

    private fun getLoggedInUser() {
        ioScope.launch {
            loggedInUserLive.postValue(
                  userRepo.getUserByUsername("Chron")
            )
        }
    }

    fun getTokeForEdit(tokeId: String) {
        ioScope.launch {
            tokeRepo.getTokeById(tokeId)?.let {
                now = it.tokeDateTime
                postTime()
                tokeForEditLive.postValue(it)
            }
        }
    }

    private fun postTime() {
        dateTimeLiveData.postValue(now)
    }

    fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        now = now.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth)
        postTime()
    }

    fun updateTime(hour: Int, minute: Int) {
        now = now.withHour(hour).withMinute(minute)
        postTime()
    }

    fun dateCreator() = "${now.dayOfMonth} / ${now.monthValue} / ${now.year}"

    fun timeCreator(): String {
        val hour: String = now.hour.toString()
        val minuteCalendar = now.minute
        val minutes: String = if (minuteCalendar < 10) {
            "0$minuteCalendar"
        } else {
            minuteCalendar.toString()
        }
        return "$hour:$minutes"
    }
}