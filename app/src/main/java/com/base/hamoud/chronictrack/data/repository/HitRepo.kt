package com.base.hamoud.chronictrack.data.repository

import androidx.annotation.WorkerThread
import com.base.hamoud.chronictrack.data.dao.HitDao
import com.base.hamoud.chronictrack.data.entity.Toke
import java.util.*

class HitRepo(private val hitDao: HitDao) {


    @WorkerThread
    fun insert(toke: Toke): Long {
        return hitDao.insert(toke)
    }

    @WorkerThread
    suspend fun getAllTokes(): MutableList<Toke> {
        return hitDao.getAllTokes().asReversed()
    }

    @WorkerThread
    suspend fun getTodaysTokes(userId: String): List<Toke> {
        // FIXME
        val today = Calendar.getInstance().get(Calendar.DATE)
        val thisMonth = Calendar.getInstance().get(Calendar.MONTH)
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        val date = "$today / $thisMonth / $thisYear"
        return getTokesByUserForDate(userId, date)
    }

    @WorkerThread
    suspend fun getTokesByUserForDate(user_id: String, date: String): List<Toke> {
        return hitDao.getTokeByUserIdAndDate(user_id, date)
    }

    @WorkerThread
    suspend fun getAllTokesByUser(userId: String): List<Toke> {
        return hitDao.getTokeByUserId(userId)
    }

    @WorkerThread
    suspend fun deleteAllTokesByUser(userId: String) {
        hitDao.deleteTokeByUserId(userId)
    }

    @WorkerThread
    suspend fun deleteAllTokes() {
        hitDao.deleteAll()
    }

    @WorkerThread
    fun deleteToke(toke: Toke) {
        hitDao.delete(toke)
    }
}