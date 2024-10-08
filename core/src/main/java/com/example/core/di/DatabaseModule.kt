package com.example.core.di

import android.content.Context
import androidx.room.Room
import com.example.core.data.local.AppDatabase
import com.example.history.data.local.LocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java,"app_database").build()

    @Provides
    fun provideLocationDao(appDatabase: AppDatabase) : LocationDao = appDatabase.locationDao()
}