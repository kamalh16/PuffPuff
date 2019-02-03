package com.base.hamoud.chronictrack.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.base.hamoud.chronictrack.data.entity.Hit

@Dao
abstract class HitDao: BaseDao<Hit> {

    /**
     * Get Hit by userId
     *
     * @param userId userId to look for
     * @return list of hits in the table by that user
     */
    @Query("SELECT * FROM HIT_TABLE WHERE user_id = :userId")
    abstract fun getHitByUserId(userId: String): List<Hit>

    /**
     * Get all Hits in table db
     *
     * @return List of hits in db
     */
    @Query("SELECT * FROM HIT_TABLE")
    abstract fun getAllHits(): List<Hit>

    /**
     * Delete all hits in the table by the user with the userId
     *
     * @param userId to look for
     */
    @Query("DELETE FROM HIT_TABLE WHERE user_id = :userId")
    abstract fun deleteHitByUserId(userId: String)
}