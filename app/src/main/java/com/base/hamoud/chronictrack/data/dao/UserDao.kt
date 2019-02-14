package com.base.hamoud.chronictrack.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.base.hamoud.chronictrack.data.entity.User

@Dao
abstract class UserDao: BaseDao<User> {

    /**
     * Get a user by id
     *
     * @return user from the user_table with that id
     */
    @Query("SELECT * FROM USER_TABLE WHERE id = :id")
    abstract fun getUserById(id: String): User

    /**
     * Get a user by id
     *
     * @return user from the user_table with that id
     */
    @Query("SELECT * FROM USER_TABLE WHERE username = :username")
    abstract fun getUserByUsername(username: String): User

    /**
     * Delete user with id
     */
    @Query("DELETE FROM USER_TABLE WHERE id = :id")
    abstract fun deleteUserById(id: String)

    /**
     * Delete all users
     */
    @Query("DELETE FROM USER_TABLE")
    abstract fun deleteAllUsers()

}