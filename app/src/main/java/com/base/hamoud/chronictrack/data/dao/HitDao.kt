package com.base.hamoud.chronictrack.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.base.hamoud.chronictrack.data.entity.Hit

@Dao
abstract class HitDao : BaseDao<Hit> {

    /**
     * Get Hit by [userId]
     *
     * @param userId to look for
     * @return list of hits in the table by that user
     */
    @Query("SELECT * FROM HIT_TABLE WHERE user_id = :userId")
    abstract suspend fun getHitByUserId(userId: String): List<Hit>

    /**
     * Get all Hits in table db
     *
     * @return List of hits in db
     */
    @Query("SELECT * FROM HIT_TABLE")
    abstract suspend fun getAllHits(): List<Hit>

    /**
     * Get all hits by [userId] for specific [date] dd-mm-yyyy
     *
     * @return List of hits by [userId] and [date]
     */
    @Query("SELECT * FROM HIT_TABLE WHERE user_id = :userId AND hit_date = :date")
    abstract suspend fun getHitByUserIdAndDate(userId: String, date: String): List<Hit>

    /**
     * Delete all hits in the table by the user with the [userId]
     *
     * @param userId to look for
     */
    @Query("DELETE FROM HIT_TABLE WHERE user_id = :userId")
    abstract suspend fun deleteHitByUserId(userId: String)


}