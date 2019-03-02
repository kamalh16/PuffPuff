package com.base.hamoud.chronictrack.data.converter

import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object OffsetDateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @JvmStatic
    @TypeConverter
    fun toOffsetDateTime(value: String): OffsetDateTime? {
        return formatter.parse(value, OffsetDateTime::from)
    }

    @JvmStatic
    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime): String? {
        return date.format(formatter)
    }
}