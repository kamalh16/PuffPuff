package com.base.hamoud.chronictrack.data.repository

import androidx.annotation.WorkerThread
import com.base.hamoud.chronictrack.data.dao.TokeDao
import com.base.hamoud.chronictrack.data.entity.Toke
import org.joda.time.DateTime

class TokeRepo(private val tokeDao: TokeDao) {

    @WorkerThread
    fun insert(toke: Toke): Long {
        return tokeDao.insert(toke)
    }

    @WorkerThread
    fun update(toke: Toke) {
        return tokeDao.update(toke)
    }

    @WorkerThread
    suspend fun getTokeById(tokeId: String): Toke? {
        return tokeDao.getTokeById(tokeId)
    }

    @WorkerThread
    suspend fun getAllTokes(): MutableList<Toke> {
        return tokeDao.getAllTokes().asReversed()
    }

    @WorkerThread
    suspend fun getTodaysTokes(): List<Toke> {
        return tokeDao.getAllTokes()
            // get Tokes that are from the current day
            .filter { DateTime(it.tokeDateTime).dayOfWeek == DateTime.now().dayOfWeek }
    }

    @WorkerThread
    suspend fun getThisWeeksTokes(): List<Toke> {
        return tokeDao.getAllTokes()
            // get Tokes that are from the current week
            .filter { DateTime(it.tokeDateTime).weekOfWeekyear == DateTime.now().weekOfWeekyear }
    }

    @WorkerThread
    suspend fun getLastTokeTaken(): Toke? {
        val todaysTokes = getTodaysTokes()
        if (todaysTokes.isNotEmpty()) {
            return todaysTokes.first()
        }
        return null
    }

    @WorkerThread
    suspend fun deleteAllTokes() {
        tokeDao.deleteAll()
    }

    @WorkerThread
    fun deleteToke(toke: Toke) {
        tokeDao.delete(toke)
    }
}