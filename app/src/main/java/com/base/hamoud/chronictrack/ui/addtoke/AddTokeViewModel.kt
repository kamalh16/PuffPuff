package com.base.hamoud.chronictrack.ui.addtoke

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.TokeRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTokeViewModel(application: Application) : BaseAndroidViewModel(application) {

    var userRepo: UserRepo = UserRepo(db.userDao())
    var tokeRepo: TokeRepo = TokeRepo(db.tokeDao())

    var loggedInUserLive: MutableLiveData<User> = MutableLiveData()

    init {
        getLoggedInUser()
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insertToke(toke: Toke) = ioScope.launch {
        tokeRepo.insert(toke)
    }

    private fun getLoggedInUser() {
        ioScope.launch(Dispatchers.IO) {
            loggedInUserLive.postValue(
                  userRepo.getUserByUsername("Chron")
            )
        }
    }

}