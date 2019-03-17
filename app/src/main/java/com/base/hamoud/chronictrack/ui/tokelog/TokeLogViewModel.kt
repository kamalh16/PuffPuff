package com.base.hamoud.chronictrack.ui.tokelog

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.TokeRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import kotlinx.coroutines.launch

class TokeLogViewModel(application: Application) : BaseAndroidViewModel(application) {

    var userRepo: UserRepo = UserRepo(db.userDao())
    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    var loggedInUserLive: MutableLiveData<User> = MutableLiveData()
    var userTokeListLive: MutableLiveData<List<Toke>> = MutableLiveData()

    init {
        getLoggedInUser()
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

    private fun getLoggedInUser() {
        ioScope.launch {
            loggedInUserLive.postValue(
                  userRepo.getUserByUsername("Chron")
            )
        }
    }

}