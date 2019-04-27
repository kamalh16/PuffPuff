package com.base.hamoud.puffpuff.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.base.hamoud.puffpuff.data.entity.Toke

@Dao
abstract class TokeDao : BaseDao<Toke> {

    /**
     * Get a [Toke] by it's [tokeId]
     *
     * @return a [Toke] if found else [null]
     */
    @Query("SELECT * FROM TOKE_TABLE WHERE id = :tokeId")
    abstract suspend fun getTokeById(tokeId: String): Toke?

    /**
     * Get all [Toke]s in table db
     *
     * @return List of hits in db
     */
    @Query("SELECT * FROM TOKE_TABLE ORDER BY toke_date_time DESC")
    abstract suspend fun getAllTokes(): MutableList<Toke>

    /**
     * Get the first ever saved Toke's datetime.
     *
     * @return if found, [Long] datetime otherwise null
     */
    @Query("SELECT toke_date_time FROM TOKE_TABLE ORDER BY toke_date_time ASC LIMIT 1")
    abstract suspend fun getFirstEverTokeDateTime(): Long?

    /**
     * Delete all [Toke]s in the table
     */
    @Query("DELETE FROM TOKE_TABLE")
    abstract suspend fun deleteAll()

}