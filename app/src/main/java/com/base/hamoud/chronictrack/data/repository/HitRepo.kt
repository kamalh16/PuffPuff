package com.base.hamoud.chronictrack.data.repository

import androidx.annotation.WorkerThread
import com.base.hamoud.chronictrack.data.dao.HitDao
import com.base.hamoud.chronictrack.data.entity.Hit
import java.time.LocalDate

class HitRepo(private val hitDao: HitDao) {


    @WorkerThread
    fun insert(hit: Hit): Long {
        return hitDao.insert(hit)
    }

    @WorkerThread
    fun getAllHits(): List<Hit> {
        return hitDao.getAllHits()
    }

    @WorkerThread
    fun getTodaysHits(today: Int): List<Hit> {
        val hits = hitDao.getAllHits()
        val hitsToday: MutableList<Hit> = listOf<Hit>().toMutableList()
        hits.forEach { hit ->
            val hitDate = (hit.hitTime.subSequence(0, hit.hitTime.indexOf("-")).trim())
            val date = LocalDate.parse(hitDate)
            if (date.dayOfMonth == today) {
                hitsToday.add(hit)
            }
        }
        return hitsToday
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