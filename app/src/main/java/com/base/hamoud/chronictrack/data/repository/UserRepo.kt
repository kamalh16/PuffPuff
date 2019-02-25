package com.base.hamoud.chronictrack.data.repository

import androidx.annotation.WorkerThread
import com.base.hamoud.chronictrack.data.dao.UserDao
import com.base.hamoud.chronictrack.data.entity.User

class UserRepo(private val userDao: UserDao) {

    @WorkerThread
    fun insert(user: User): Long {
        return userDao.insert(user)
    }

    @WorkerThread
    suspend fun getUserById(userId: String): User {
        return userDao.getUserById(userId)
    }

    @WorkerThread
    suspend fun getUserByUsername(username: String): User {
        return userDao.getUserByUsername(username)
    }
}