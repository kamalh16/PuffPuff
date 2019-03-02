package com.base.hamoud.chronictrack.data.repository

import androidx.annotation.WorkerThread
import com.base.hamoud.chronictrack.data.dao.HitDao
import com.base.hamoud.chronictrack.data.entity.Hit
import java.util.*

class HitRepo(private val hitDao: HitDao) {


    @WorkerThread
    fun insert(hit: Hit): Long {
        return hitDao.insert(hit)
    }

    @WorkerThread
    suspend fun getAllTokes(): MutableList<Hit> {
        return hitDao.getAllTokes().asReversed()
    }

    @WorkerThread
    suspend fun getTodaysTokes(userId: String): List<Hit> {
        // FIXME
        val today = Calendar.getInstance().get(Calendar.DATE)
        val thisMonth = Calendar.getInstance().get(Calendar.MONTH)
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        val date = "$today / $thisMonth / $thisYear"
        return getTokesByUserForDate(userId, date)
    }

    @WorkerThread
    suspend fun getTokesByUserForDate(user_id: String, date: String): List<Hit> {
        return hitDao.getTokeByUserIdAndDate(user_id, date)
    }

    @WorkerThread
    suspend fun getAllTokesByUser(userId: String): List<Hit> {
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
    fun deleteToke(hit: Hit) {
        hitDao.delete(hit)
    }
}