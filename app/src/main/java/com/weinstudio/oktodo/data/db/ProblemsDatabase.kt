package com.weinstudio.oktodo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.weinstudio.oktodo.data.db.converter.PriorityConverter
import com.weinstudio.oktodo.data.db.dao.ProblemsDao
import com.weinstudio.oktodo.data.entity.Problem

@Database(entities = [Problem::class], version = 1, exportSchema = false)
@TypeConverters(PriorityConverter::class)
abstract class ProblemsDatabase : RoomDatabase() {

    abstract fun problemsDao(): ProblemsDao

    companion object {

        private const val DATABASE_NAME = "memoria_db"

        @Volatile
        private var instance: ProblemsDatabase? = null

        fun getDatabase(context: Context): ProblemsDatabase {
            return instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProblemsDatabase::class.java, DATABASE_NAME

                ).allowMainThreadQueries().build()

                this.instance = instance

                instance
            }
        }
    }
}