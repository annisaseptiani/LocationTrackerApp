package com.example.domain.usecase

import com.example.domain.repository.HistoryLocationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.mock


class DeleteAllUseCaseTest{

    private lateinit var deleteAllUseCase: DeleteAllUseCase
    private lateinit var repository: HistoryLocationRepository

    @Before
    fun setUp() {
        repository = mock() // Mock the repository
        deleteAllUseCase = DeleteAllUseCase(repository)
    }

    @Test
    fun `invoke should call deleteAll on repository`() = runBlocking {
        // Call the use case
        deleteAllUseCase.invoke()

        // Verify that the deleteAll method was called
        verify(repository).deleteAll()
    }
}