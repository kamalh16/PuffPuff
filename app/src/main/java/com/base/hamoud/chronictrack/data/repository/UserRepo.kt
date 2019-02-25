package com.base.hamoud.chronictrack.data.repository

import androidx.annotation.WorkerThread
import com.base.hamoud.chronictrack.data.dao.UserDao
import com.base.hamoud.chronictrack.data.entity.User

class UserRepo(private val userDao: UserDao) {

    /**
     * Insert a user if they don't already exist in the database.
     *
     * @return [User] existing user if exists
     */
    @WorkerThread
    fun insert(user: User): User? {
        val existingUser = userDao.getUserByUsername(user.username)
        if (existingUser == null) {
            userDao.insert(user)
        }
        return userDao.getUserByUsername(user.username)
    }

    @WorkerThread
    fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)
    }

    @WorkerThread
    fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }
}