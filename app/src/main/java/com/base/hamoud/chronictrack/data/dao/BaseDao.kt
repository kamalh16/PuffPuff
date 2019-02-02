package com.base.hamoud.chronictrack.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Transaction
import androidx.room.Update

interface BaseDao<T> {

    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert
    fun insert(obj: T): Long

    /**
     * Insert an array of objects in the database.
     *
     * @param obj the objects to be inserted.
     */
    @Insert
    fun insert(vararg obj: T)

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    fun update(obj: T)

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    fun delete(obj: T)

    /**
     * Upsert(Insert if fails then Update) an object into the database
     *
     * @param obj the object to be upserted
     */
    @Transaction
    fun upsert(obj: T) {
        val id = insert(obj)
        if(id == -1L) update(obj)
    }
}