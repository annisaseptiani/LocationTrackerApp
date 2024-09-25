package com.example.history.data.mapper

import com.example.domain.model.Location
import com.example.history.data.entity.LocationEntity
import javax.inject.Inject

class LocationMapper @Inject constructor() {
    fun MapLocationData(entity : LocationEntity) : Location {
        return Location(latitude = entity.latitude,
            longitude = entity.longitude,
            timestamp = entity.timeStamp,
            isOnline = entity.isOffline)
    }
}