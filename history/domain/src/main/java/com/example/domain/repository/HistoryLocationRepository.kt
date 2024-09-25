package com.example.domain.repository

import com.example.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface HistoryLocationRepository {
    fun getLocationHistory() : Flow<List<Location>>
    suspend fun saveLocation(location: Location)
    suspend fun deleteAll()
}