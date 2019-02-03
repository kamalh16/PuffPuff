package com.base.hamoud.chronictrack

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.hamoud.chronictrack.data.TokesDatabase
import com.base.hamoud.chronictrack.data.entity.Hit
import com.base.hamoud.chronictrack.data.entity.User
import com.base.hamoud.chronictrack.data.repository.HitReposiroty
import com.base.hamoud.chronictrack.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ChronicTrackerViewModel(application: Application): AndroidViewModel(application) {

    var userRepository: UserRepository
    var hitReposiroty: HitReposiroty
    var allUserHits: List<Hit>
    var db: TokesDatabase = TokesDatabase.getDatabase(application)

    private var parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    init {
        userRepository = UserRepository(db.userDao())
        hitReposiroty = HitReposiroty(db.hitDao())
        allUserHits = hitReposiroty.getAllHits()!!
    }

    fun insertUser(user: User) = scope.launch(Dispatchers.IO) {
        userRepository.insert(user)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}