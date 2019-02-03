package com.base.hamoud.chronictrack

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.hamoud.chronictrack.data.TokesDatabase
import com.base.hamoud.chronictrack.data.entity.Hit
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.HitRepo
import com.base.hamoud.chronictrack.data.repository.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ChronicTrackerViewModel(application: Application): AndroidViewModel(application) {

    var userRepo: UserRepo
    var hitRepo: HitRepo
    var allUserHits: List<Hit>
    var db: TokesDatabase = TokesDatabase.getDatabase(application)

    private var parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    init {
        userRepo = UserRepo(db.userDao())
        hitRepo = HitRepo(db.hitDao())
        allUserHits = hitRepo.getAllHits()!!
    }

    fun insertUser(user: User) = scope.launch(Dispatchers.IO) {
        userRepo.insert(user)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}