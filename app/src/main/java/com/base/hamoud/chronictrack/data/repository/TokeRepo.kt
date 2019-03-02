package com.base.hamoud.chronictrack.data.repository

import androidx.annotation.WorkerThread
import com.base.hamoud.chronictrack.data.dao.TokeDao
import com.base.hamoud.chronictrack.data.entity.Toke
import java.time.OffsetDateTime

class TokeRepo(private val tokeDao: TokeDao) {


    @WorkerThread
    fun insert(toke: Toke): Long {
        return tokeDao.insert(toke)
    }

    @WorkerThread
    suspend fun getAllTokes(): MutableList<Toke> {
        return tokeDao.getAllTokes().asReversed()
    }

    @WorkerThread
    suspend fun getTodaysTokes(userId: String): List<Toke> {
        // FIXME
        val today = OffsetDateTime.now()

        val todaysHits: List<Toke> = getAllTokes().filter {
            (it.tokeDate.dayOfYear == today.dayOfYear && it.tokeDate.year == today.year)
        }
        return todaysHits
    }

    @WorkerThread
    suspend fun getTokesByUserForDate(user_id: String, date: OffsetDateTime): List<Toke> {
        return tokeDao.getTokeByUserIdAndDate(user_id, date)
    }

    @WorkerThread
    suspend fun getAllTokesByUser(userId: String): List<Toke> {
        return tokeDao.getTokeByUserId(userId)
    }

    @WorkerThread
    suspend fun deleteAllTokesByUser(userId: String) {
        tokeDao.deleteTokeByUserId(userId)
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