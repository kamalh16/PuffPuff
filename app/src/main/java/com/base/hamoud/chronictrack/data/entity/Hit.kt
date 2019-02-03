package com.base.hamoud.chronictrack.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "hit_table",
    foreignKeys = arrayOf(
        ForeignKey(entity = User::class,
            parentColumns =["id"],
            childColumns = arrayOf("user_id")
        )
    ))
data class Hit(
    @PrimaryKey @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "hit_time") val hitTime: String = Date().toString(),
    @ColumnInfo(name = "weed_type") val hitType: String = "Sativa",
    @ColumnInfo(name = "strain") val strain: String = "Gods Goat",
    @ColumnInfo(name = "tool_used") val toolUsed: String = "Bong"
)