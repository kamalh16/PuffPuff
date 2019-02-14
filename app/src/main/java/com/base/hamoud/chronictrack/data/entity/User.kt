package com.base.hamoud.chronictrack.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_table",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["username"], unique = true)
    ])
data class User(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "username") val username: String
)