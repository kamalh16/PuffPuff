package com.base.hamoud.chronictrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.base.hamoud.chronictrack.data.dao.TokeDao
import com.base.hamoud.chronictrack.data.dao.UserDao
import com.base.hamoud.chronictrack.data.entity.Toke
import com.base.hamoud.chronictrack.data.entity.User

@Database(
    entities = [
        User::class,
        Toke::class],
    version = 2
)
abstract class TokesDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun hitDao(): TokeDao

    companion object {

        @Volatile
        private var INSTANCE: TokesDatabase? = null

        fun getInstance(context: Context): TokesDatabase {
            if (INSTANCE == null) {
                synchronized(TokesDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            TokesDatabase::class.java, "Tokes_database"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}