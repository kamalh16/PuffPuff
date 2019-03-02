package com.base.hamoud.chronictrack.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.base.hamoud.chronictrack.data.entity.Toke

@Dao
abstract class TokeDao : BaseDao<Toke> {

    /**
     * Get [Toke] by [userId]
     *
     * @param userId to look for
     * @return list of hits in the table by that user
     */
    @Query("SELECT * FROM TOKE_TABLE WHERE user_id = :userId")
    abstract suspend fun getTokeByUserId(userId: String): List<Toke>

    /**
     * Get all [Toke]s in table db
     *
     * @return List of hits in db
     */
    @Query("SELECT * FROM TOKE_TABLE")
    abstract suspend fun getAllTokes(): MutableList<Toke>

    /**
     * Get all [Toke]s by [userId] for specific [date] dd-mm-yyyy
     *
     * @return List of hits by [userId] and [date]
     */
    @Query("SELECT * FROM TOKE_TABLE WHERE user_id = :userId AND toke_date = :date")
    abstract suspend fun getTokeByUserIdAndDate(userId: String, date: String): List<Toke>

    /**
     * Delete all [Toke]s in the table by the user with the [userId]
     *
     * @param userId to look for
     */
    @Query("DELETE FROM TOKE_TABLE WHERE user_id = :userId")
    abstract suspend fun deleteTokeByUserId(userId: String)


    /**
     * Delete all [Toke]s in the table
     */
    @Query("DELETE FROM TOKE_TABLE")
    abstract suspend fun deleteAll()

}