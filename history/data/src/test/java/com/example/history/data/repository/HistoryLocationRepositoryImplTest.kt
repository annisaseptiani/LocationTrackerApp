package com.example.history.data.repository

import com.example.domain.model.Location
import com.example.history.data.entity.LocationEntity
import com.example.history.data.local.LocationDao
import com.example.history.data.mapper.LocationMapper
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


@OptIn(ExperimentalCoroutinesApi::class)
class HistoryLocationRepositoryImplTest {

    @Mock
    private lateinit var dao: LocationDao

    @Mock
    private lateinit var mapper: LocationMapper

    private lateinit var repository: HistoryLocationRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = HistoryLocationRepositoryImpl(dao, mapper)
    }

    @Test
    fun `getLocationHistory should return mapped locations`() = runTest {
        // Arrange
        val locationEntity = LocationEntity(latitude = 10.0, longitude = 20.0, timeStamp = 123456789, isOffline = true)
        val location = Location(latitude = 10.0, longitude = 20.0, timestamp = 123456789, isOnline = true)
        `when`(dao.getAllLocations()).thenReturn(flowOf(listOf(locationEntity)))
        `when`(mapper.MapLocationData(locationEntity)).thenReturn(location)

        // Act
        val result: Flow<List<Location>> = repository.getLocationHistory()

        // Assert
        result.collect { locations ->
            assertEquals(1, locations.size)
            assertEquals(location, locations[0])
        }

        verify(dao).getAllLocations()
        verify(mapper).MapLocationData(locationEntity)
    }

    @Test
    fun `saveLocation should call insertLocation on dao`() = runBlocking {
        // Arrange
        val location = Location(latitude = 10.0, longitude = 20.0, timestamp = 123456789, isOnline = true)
        val locationEntity = LocationEntity(latitude = 10.0, longitude = 20.0, timeStamp = 123456789, isOffline = true)

        // Act
        repository.saveLocation(location)

        // Assert
        verify(dao).insertLocation(locationEntity)
    }

    @Test
    fun `deleteAll should call deleteAll on dao`() = runBlocking {
        // Act
        repository.deleteAll()

        // Assert
        verify(dao).deleteAll()
    }
}