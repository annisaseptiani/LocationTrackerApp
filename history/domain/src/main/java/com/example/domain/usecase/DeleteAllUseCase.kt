package com.example.domain.usecase

import com.example.domain.repository.HistoryLocationRepository
import javax.inject.Inject

class DeleteAllUseCase @Inject constructor(
    private val repository: HistoryLocationRepository
) {
    suspend operator fun invoke() = repository.deleteAll()
}