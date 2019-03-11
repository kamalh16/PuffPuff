package com.base.hamoud.chronictrack.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.base.hamoud.chronictrack.data.entity.Toke
import java.time.OffsetDateTime

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
    @Query("SELECT * FROM TOKE_TABLE")
    abstract suspend fun getAllTokes(): MutableList<Toke>

    /**
     * Get all hits by [userId] for specific [date] dd-mm-yyyy
     *
     * @return List of hits by [userId] and [date]
     */
    @Query("SELECT * FROM TOKE_TABLE WHERE toke_date_time = :date ORDER BY datetime(toke_date_time)")
    abstract suspend fun getTokeByUserIdAndDate(date: OffsetDateTime): List<Toke>

    /**
     * Delete all [Toke]s in the table
     */
    @Query("DELETE FROM TOKE_TABLE")
    abstract suspend fun deleteAll()

}