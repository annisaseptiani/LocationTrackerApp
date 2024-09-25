package com.example.domain.usecase

import com.example.domain.model.Location
import com.example.domain.repository.HistoryLocationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.mock


class GetLocationUseCaseTest {
    private lateinit var saveLocationUseCase: SaveLocationUseCase
    private lateinit var repository: HistoryLocationRepository

    @Before
    fun setUp() {
        repository = mock()
        saveLocationUseCase = SaveLocationUseCase(repository)
    }

    @Test
    fun `invoke should call saveLocation on repository`() = runBlocking {
        val location = Location(1.0, 2.0, System.currentTimeMillis(), isOnline = true)

        saveLocationUseCase.invoke(location)

        verify(repository).saveLocation(location)
    }
}