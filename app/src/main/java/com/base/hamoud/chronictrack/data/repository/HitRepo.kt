package com.base.hamoud.chronictrack.data.repository

import androidx.annotation.WorkerThread
import com.base.hamoud.chronictrack.data.dao.HitDao
import com.base.hamoud.chronictrack.data.entity.Hit
import java.time.OffsetDateTime
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
        val today = OffsetDateTime.now()

        val todaysHits: List<Hit> = getAllHits().filter {
            (it.hitDate.dayOfYear == today.dayOfYear && it.hitDate.year == today.year)
        }
        return todaysHits
    }

    @WorkerThread
    suspend fun getHitsByUserForDate(user_id: String, date: OffsetDateTime): List<Hit> {
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