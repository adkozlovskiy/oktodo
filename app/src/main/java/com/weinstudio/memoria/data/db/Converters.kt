package com.weinstudio.memoria.data.db

import androidx.room.TypeConverter
import com.weinstudio.memoria.data.entity.enums.Priority

class Converters {

    @TypeConverter
    fun toPriority(value: String) = enumValueOf<Priority>(value)

    @TypeConverter
    fun fromPriority(value: Priority) = value.name

}