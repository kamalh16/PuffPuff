package com.base.hamoud.chronictrack.data.repository

import androidx.annotation.WorkerThread
import com.base.hamoud.chronictrack.data.dao.UserDao
import com.base.hamoud.chronictrack.data.entity.User

class UserRepository(private val userDao: UserDao) {

    @WorkerThread
    fun insert(user: User) {
        userDao.insert(user)
    }

    @WorkerThread
    fun getUserById(userId: String): User {
        return userDao.getUserById(userId)
    }
}