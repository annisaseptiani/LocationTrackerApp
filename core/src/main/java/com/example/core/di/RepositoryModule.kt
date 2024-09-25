package com.example.core.di

import com.example.domain.repository.HistoryLocationRepository
import com.example.domain.usecase.DeleteAllUseCase
import com.example.domain.usecase.GetLocationUseCase
import com.example.domain.usecase.SaveLocationUseCase
import com.example.history.data.local.LocationDao
import com.example.history.data.mapper.LocationMapper
import com.example.history.data.repository.HistoryLocationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providesLocationRepository(dao : LocationDao,
                                   locationMapper: LocationMapper) : HistoryLocationRepository {
        return HistoryLocationRepositoryImpl(dao = dao, mapper = locationMapper)
    }

    @Provides
    @Singleton
    fun provideLocationMapper() : LocationMapper {
        return LocationMapper()
    }

    @Provides
    @Singleton
    fun provideSaveLocationUseCase(historyLocationRepository: HistoryLocationRepository) : SaveLocationUseCase {
        return SaveLocationUseCase(historyLocationRepository)
    }

    @Provides
    @Singleton
    fun provideGetListLocationUseCase(historyLocationRepository: HistoryLocationRepository) : GetLocationUseCase {
        return GetLocationUseCase(historyLocationRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteLocationUseCase(historyLocationRepository: HistoryLocationRepository) : DeleteAllUseCase {
        return DeleteAllUseCase(historyLocationRepository)
    }
}