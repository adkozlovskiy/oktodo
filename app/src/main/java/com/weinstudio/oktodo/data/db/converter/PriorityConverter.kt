package com.weinstudio.oktodo.data.db.converter

import androidx.room.TypeConverter
import com.weinstudio.oktodo.data.entity.enums.Importance

class PriorityConverter {

    @TypeConverter
    fun toPriority(value: Int) = enumValues<Importance>()[value]

    @TypeConverter
    fun fromPriority(value: Importance) = value.ordinal

}