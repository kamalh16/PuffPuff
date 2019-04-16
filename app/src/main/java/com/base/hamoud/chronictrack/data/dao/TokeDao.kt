package com.base.hamoud.chronictrack.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.base.hamoud.chronictrack.data.entity.Toke

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
     * Delete all [Toke]s in the table
     */
    @Query("DELETE FROM TOKE_TABLE")
    abstract suspend fun deleteAll()

}