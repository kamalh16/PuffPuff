package com.base.hamoud.chronictrack.ui.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MainViewModel(application: Application) : BaseAndroidViewModel(application) {

    // set logged in user
    private var user: User = User(UUID.randomUUID().toString(), "Chron")

    var userRepo: UserRepo = UserRepo(db.userDao())

    var loggedInUserLive: MutableLiveData<User> = MutableLiveData()

    init {
        attemptInsertUser(user)
        Timber.i("attemptInsertUser: $user")
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    private fun attemptInsertUser(user: User) {
        ioScope.launch(Dispatchers.IO) {
            loggedInUserLive.postValue(
                  userRepo.insert(user)
            )
        }
    }

}