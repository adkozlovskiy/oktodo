package com.weinstudio.oktodo.di

import android.app.Application
import androidx.room.Room
import com.weinstudio.oktodo.data.db.ProblemsDao
import com.weinstudio.oktodo.data.db.ProblemsDatabase
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
    fun provideProblemsDatabase(appContext: Application): ProblemsDatabase {
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