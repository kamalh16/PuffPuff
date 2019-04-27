package com.base.hamoud.puffpuff.data.dao

import androidx.room.*

interface BaseDao<T> {

    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
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