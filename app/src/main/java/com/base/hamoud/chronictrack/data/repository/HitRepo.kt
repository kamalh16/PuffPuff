package com.base.hamoud.chronictrack.data.repository

import androidx.annotation.WorkerThread
import com.base.hamoud.chronictrack.data.dao.HitDao
import com.base.hamoud.chronictrack.data.entity.Hit

class HitRepo(private val hitDao: HitDao) {


    @WorkerThread
    fun insert(hit: Hit): Long {
        return hitDao.insert(hit)
    }

    @WorkerThread
    fun getAllHits(): List<Hit>? {
        return hitDao.getAllHits()
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