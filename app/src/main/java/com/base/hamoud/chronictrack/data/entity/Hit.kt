package com.base.hamoud.chronictrack.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "hit_table",
        foreignKeys = [
            ForeignKey(entity = User::class,
                    parentColumns = ["id"],
                    childColumns = arrayOf("user_id")
            )
        ]
)
data class Hit(
        @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "user_id") var userId: String,
        @ColumnInfo(name = "hit_time") val hitTime: String = Date().toString(),
        @ColumnInfo(name = "weed_type") val hitType: String = arrayOf(
                "Sativa",
                "Indica",
                "Hybrid",
                "Concentrate").random(),
        @ColumnInfo(name = "strain") val strain: String = arrayOf(
                "Gods Goat",
                "Goats Testicles",
                "Super Lemon Haze",
                "Blueberry").random(),
        @ColumnInfo(name = "tool_used") val toolUsed: String = arrayOf(
                "Water Bong",
                "Pipe",
                "Vape",
                "Joint",
                "Dab").random()
)