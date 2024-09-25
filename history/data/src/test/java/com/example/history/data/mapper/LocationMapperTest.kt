package com.example.history.data.mapper

import com.example.history.data.entity.LocationEntity
import org.junit.Assert.assertEquals
import org.junit.Test


class LocationMapperTest {

    private val locationMapper = LocationMapper()

    @Test
    fun `mapLocationData should correctly map LocationEntity to Location`() {
        // Given: A LocationEntity
        val entity = LocationEntity(
            latitude = 1.2345,
            longitude = 6.7890,
            timeStamp = 123456789,
            isOffline = true
        )

        // When: mapLocationData is called
        val location = locationMapper.MapLocationData(entity)

        // Then: Assert the mapping is correct
        assertEquals(entity.latitude, location.latitude, 0.0)
        assertEquals(entity.longitude, location.longitude, 0.0)
        assertEquals(entity.timeStamp, location.timestamp)
        assertEquals(entity.isOffline, location.isOnline)
    }
}