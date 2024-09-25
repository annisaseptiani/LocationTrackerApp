package com.example.history.data.repository

import com.example.domain.model.Location
import com.example.domain.repository.HistoryLocationRepository
import com.example.history.data.entity.LocationEntity
import com.example.history.data.local.LocationDao
import com.example.history.data.mapper.LocationMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryLocationRepositoryImpl @Inject constructor(
    private val dao: LocationDao,
    private val mapper : LocationMapper) :
    HistoryLocationRepository {
    override fun getLocationHistory(): Flow<List<Location>> {
        return dao.getAllLocations().map {data->
            data.map { dataloc -> mapper.MapLocationData(dataloc) }
        }
    }

    override suspend fun saveLocation(location: Location) {
        dao.insertLocation(LocationEntity(latitude = location.latitude,
            longitude = location.longitude, timeStamp = location.timestamp,
            isOffline = location.isOnline))
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}