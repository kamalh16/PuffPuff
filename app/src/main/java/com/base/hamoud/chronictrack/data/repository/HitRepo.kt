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
    suspend fun getAllHits(): MutableList<Hit> {
        return hitDao.getAllHits().asReversed()
    }

    @WorkerThread
    suspend fun getTodaysHits(userId: String): List<Hit> {
        // FIXME
        val today = Calendar.getInstance().get(Calendar.DATE)
        val thisMonth = Calendar.getInstance().get(Calendar.MONTH)
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        val date = "$today / $thisMonth / $thisYear"
        return getHitsByUserForDate(userId, date)
    }

    @WorkerThread
    suspend fun getHitsByUserForDate(user_id: String, date: String): List<Hit> {
        return hitDao.getHitByUserIdAndDate(user_id, date)
    }

    @WorkerThread
    suspend fun getAllHitsByUser(userId: String): List<Hit> {
        return hitDao.getHitByUserId(userId)
    }

    @WorkerThread
    suspend fun deleteAllHitsByUser(userId: String) {
        hitDao.deleteHitByUserId(userId)
    }

    @WorkerThread
    fun deleteHit(hit: Hit) {
        hitDao.delete(hit)
    }
}