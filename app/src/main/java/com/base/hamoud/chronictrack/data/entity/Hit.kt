package com.base.hamoud.chronictrack.data.entity

import androidx.room.*
import com.base.hamoud.chronictrack.data.converter.OffsetDateTimeConverter
import java.time.OffsetDateTime
import java.util.*

@Entity(tableName = "hit_table",
        foreignKeys = [
            ForeignKey(entity = User::class,
                    parentColumns = ["id"],
                    childColumns = ["user_id"],
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
            )
        ],
        indices = [
            Index(value = ["id"]),
            Index(value = ["user_id"]),
            Index(value = ["hit_date"])
        ]
)
data class Hit(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "user_id") var userId: String,
    @TypeConverters(OffsetDateTimeConverter::class)
    @ColumnInfo(name = "hit_time") val hitTime: OffsetDateTime = OffsetDateTime.now(),
    @TypeConverters(OffsetDateTimeConverter::class)
    @ColumnInfo(name = "hit_date") val hitDate: OffsetDateTime = OffsetDateTime.now(),
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