package com.base.hamoud.chronictrack.ui.addtoke

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.BaseAndroidViewModel
import com.base.hamoud.chronictrack.data.entity.Hit
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.HitRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTokeViewModel(application: Application) : BaseAndroidViewModel(application) {

    var userRepo: UserRepo = UserRepo(db.userDao())
    var hitRepo: HitRepo = HitRepo(db.hitDao())

    var loggedInUserLive: MutableLiveData<User> = MutableLiveData()

    init {
        getLoggedInUser()
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insertHit(hit: Hit) = ioScope.launch {
        hit.userId = loggedInUserLive.value?.id!!
        hitRepo.insert(hit)
    }

    private fun getLoggedInUser() {
        ioScope.launch(Dispatchers.IO) {
            loggedInUserLive.postValue(
                  userRepo.getUserByUsername("Chron")
            )
        }
    }

}