package com.weinstudio.oktodo.di

import android.content.Context
import androidx.room.Room
import com.weinstudio.oktodo.data.db.ProblemsDatabase
import com.weinstudio.oktodo.data.db.dao.ProblemsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    @Singleton
    fun provideProblemsDatabase(appContext: Context): ProblemsDatabase {
        val builder = Room.databaseBuilder(
            appContext,
            ProblemsDatabase::class.java, "oktodo"
        )

        builder.allowMainThreadQueries()
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideProblemsDao(problemsDatabase: ProblemsDatabase): ProblemsDao {
        return problemsDatabase.problemsDao
    }
}