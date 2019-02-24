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
    fun getAllHits(): List<Hit> {
        return hitDao.getAllHits().asReversed()
    }

    @WorkerThread
    fun getTodaysHits(userId: String): List<Hit> {
        val today = Calendar.getInstance().get(Calendar.DATE)
        val thisMonth = Calendar.getInstance().get(Calendar.MONTH)
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        val date = "$today / $thisMonth / $thisYear"
        return getHitsByUserForDate(userId, date)
    }

    @WorkerThread
    fun getHitsByUserForDate(user_id: String, date: String): List<Hit> {
        return hitDao.getHitByUserIdAndDate(user_id, date)
    }

    @WorkerThread
    fun getAllHitsByUser(userId: String): List<Hit> {
        return hitDao.getHitByUserId(userId)
    }

    @WorkerThread
    fun deleteAllHitsByUser(userId: String) {
        hitDao.deleteHitByUserId(userId)
    }
}