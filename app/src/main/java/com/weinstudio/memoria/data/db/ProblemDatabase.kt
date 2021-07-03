package com.weinstudio.memoria.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.weinstudio.memoria.data.db.dao.ProblemDao
import com.weinstudio.memoria.data.entity.Problem

@Database(entities = [Problem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ProblemDatabase : RoomDatabase() {

    abstract fun problemsDao(): ProblemDao

    companion object {

        @Volatile
        private var instance: ProblemDatabase? = null

        fun getDatabase(context: Context): ProblemDatabase {
            return instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProblemDatabase::class.java, "database"

                ).allowMainThreadQueries().build()

                this.instance = instance

                instance
            }
        }
    }
}