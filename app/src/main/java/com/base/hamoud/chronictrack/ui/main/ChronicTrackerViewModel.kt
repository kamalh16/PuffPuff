package com.base.hamoud.chronictrack.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.base.hamoud.chronictrack.data.TokesDatabase
import com.base.hamoud.chronictrack.data.entity.Hit
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.HitRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class ChronicTrackerViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    var userRepo: UserRepo
    var hitRepo: HitRepo
    var db: TokesDatabase = TokesDatabase.getDatabase(application)

    //    var allUserHits: List<Hit>

    var userHitsLive: MutableLiveData<List<Hit>>? = null

    init {
        userRepo = UserRepo(db.userDao())
        hitRepo = HitRepo(db.hitDao())
//        allUserHits = hitRepo.getAllHits() // FIXME: crashes

        Timber.i("start")
    }

    fun insertUser(user: User) = scope.launch(Dispatchers.IO) {
        userRepo.insert(user)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}