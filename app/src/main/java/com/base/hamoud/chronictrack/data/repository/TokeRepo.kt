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
    suspend fun getTokesFor(forDateTime: DateTime): MutableList<Toke> {
        return tokeDao.getAllTokes()
            // get Tokes that are from the current day
            .filter { DateTime(it.tokeDateTime).dayOfYear == forDateTime.dayOfYear }.toMutableList()
    }

    @WorkerThread
    suspend fun getThisWeeksTokes(): List<Toke> {
        return tokeDao.getAllTokes()
            // get Tokes that are from the current week
            .filter { DateTime(it.tokeDateTime).weekOfWeekyear == DateTime.now().weekOfWeekyear }
    }

    @WorkerThread
    suspend fun getLastTokeAdded(): Toke? {
        return getTokesFor(DateTime.now()).firstOrNull()
    }

    @WorkerThread
    suspend fun getLastTokedAtTime(): Long? {
        return getTokesFor(DateTime.now())
            // return the last time the user Toked at; avoids the case where
            // the countdown is a negative number when user adds a Toke in a future time
            .firstOrNull { DateTime(it.tokeDateTime).isBefore(DateTime.now()) }?.tokeDateTime
    }

    @WorkerThread
    suspend fun getFirstEverSavedTokeDateTime(): Long? {
        return tokeDao.getFirstEverTokeDateTime()
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