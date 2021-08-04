package com.weinstudio.oktodo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.weinstudio.oktodo.data.db.converter.ImportanceConverter
import com.weinstudio.oktodo.data.model.Problem

@Database(entities = [Problem::class], version = 1, exportSchema = false)
@TypeConverters(ImportanceConverter::class)
abstract class ProblemsDatabase : RoomDatabase() {

    abstract val problemsDao: ProblemsDao

}