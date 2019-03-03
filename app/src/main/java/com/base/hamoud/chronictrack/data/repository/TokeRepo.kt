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
    suspend fun getTodaysTokes():
            List<Toke> {
        val today = OffsetDateTime.now()

        return getAllTokes().filter {
            (it.tokeDateTime.dayOfYear == today.dayOfYear && it.tokeDateTime.year == today.year)
        }.sortedByDescending {
            it.tokeDateTime
        }
    }

    @WorkerThread
    suspend fun getTokesByUserForDate(date: OffsetDateTime): List<Toke> {
        return tokeDao.getTokeByUserIdAndDate(date)
    }

    @WorkerThread
    suspend fun getAllTokesByUser(): List<Toke> {
        return tokeDao.getAllTokes()
    }

    @WorkerThread
    suspend fun deleteAllTokesByUser(

    ) {
        tokeDao.deleteAll()
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