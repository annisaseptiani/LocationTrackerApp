package com.example.domain.usecase

import com.example.domain.model.Location
import com.example.domain.repository.HistoryLocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(private val repository: HistoryLocationRepository) {
    operator fun invoke(): Flow<List<Location>> = repository.getLocationHistory()
}