package com.base.hamoud.chronictrack.data.repository

import androidx.annotation.WorkerThread
import com.base.hamoud.chronictrack.data.dao.UserDao
import com.base.hamoud.chronictrack.data.entity.User

class UserRepo(private val userDao: UserDao) {

    @WorkerThread
    fun insert(user: User): User? {
        val existingUser = userDao.getUserByUsername(user.username)
        if (existingUser == null) {
            userDao.insert(user)
        }
        return userDao.getUserByUsername(user.username)
    }

    @WorkerThread
    fun getUserById(userId: String): User {
        return userDao.getUserById(userId)
    }

    @WorkerThread
    fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }
}