package com.base.hamoud.chronictrack.data.entity

import androidx.room.*
import com.base.hamoud.chronictrack.data.converter.OffsetDateTimeConverter
import java.time.OffsetDateTime
import java.util.*

@Entity(
    tableName = "toke_table",
    indices = [
        Index(value = ["id"]),
        Index(value = ["toke_date_time"])
    ]
)
data class Toke(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString(),
    @TypeConverters(OffsetDateTimeConverter::class)
    @ColumnInfo(name = "toke_date_time") val tokeDateTime: OffsetDateTime,
    @ColumnInfo(name = "weed_type") val tokeType: String = arrayOf(
        "Sativa",
        "Indica",
        "Hybrid",
        "Concentrate"
    ).random(),
    @ColumnInfo(name = "strain") val strain: String = arrayOf(
        "Gods Goat",
        "Goats Testicles",
        "Super Lemon Haze",
        "Blueberry"
    ).random(),
    @ColumnInfo(name = "tool_used") val toolUsed: String = arrayOf(
        "Water Bong",
        "Pipe",
        "Vape",
        "Joint",
        "Dab"
    ).random()
)