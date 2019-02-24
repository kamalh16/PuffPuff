package com.base.hamoud.chronictrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.base.hamoud.chronictrack.data.dao.HitDao
import com.base.hamoud.chronictrack.data.dao.UserDao
import com.base.hamoud.chronictrack.data.entity.Hit
import com.base.hamoud.chronictrack.data.entity.User

@Database(
    entities = [
        User::class,
        Hit::class],
    version = 2
)
abstract class TokesDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun hitDao(): HitDao

    companion object {
        
        @Volatile
        private var INSTANCE: TokesDatabase? = null

        fun getDatabase(context: Context): TokesDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                // create db here
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TokesDatabase::class.java,
                    "Tokes_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}