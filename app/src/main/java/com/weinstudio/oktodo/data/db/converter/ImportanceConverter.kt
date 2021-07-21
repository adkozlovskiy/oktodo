package com.weinstudio.oktodo.data.db.converter

import androidx.room.TypeConverter
import com.weinstudio.oktodo.data.model.enums.Importance

class ImportanceConverter {

    @TypeConverter
    fun toImportance(value: Int) = enumValues<Importance>()[value]

    @TypeConverter
    fun fromImportance(value: Importance) = value.ordinal

}