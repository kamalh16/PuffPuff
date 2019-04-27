package com.base.hamoud.puffpuff

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.hamoud.puffpuff.data.TokesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseAndroidViewModel(application: Application) : AndroidViewModel(application) {

    var db: TokesDatabase = TokesDatabase.getInstance(application)

    var parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO

    val ioScope = CoroutineScope(coroutineContext)

    val uiScope = CoroutineScope(Dispatchers.Main)

}