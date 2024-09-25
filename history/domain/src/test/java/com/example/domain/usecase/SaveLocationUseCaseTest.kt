package com.example.domain.usecase

import com.example.domain.model.Location
import com.example.domain.repository.HistoryLocationRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


class SaveLocationUseCaseTest {
    private lateinit var getLocationUseCase: GetLocationUseCase
    private lateinit var repository: HistoryLocationRepository

    @Before
    fun setUp() {
        repository = mock() // Mock the repository
        getLocationUseCase = GetLocationUseCase(repository)
    }

    @Test
    fun `invoke should return location history from repository`() = runBlocking {
        // Prepare fake data
        val location = Location(1.0, 2.0, System.currentTimeMillis(), isOnline = true)
        val locationList = listOf(location)

        // Mock the repository to return the flow of the fake data
        whenever(repository.getLocationHistory()).thenReturn(flowOf(locationList))

        // Call the use case and collect the result
        val result = getLocationUseCase.invoke()

        // Verify that the result is the expected location history
        result.collect { locations ->
            assertEquals(locationList, locations)
        }
    }
}