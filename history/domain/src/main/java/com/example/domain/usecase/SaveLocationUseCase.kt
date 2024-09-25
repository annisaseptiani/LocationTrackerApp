package com.example.domain.usecase

import com.example.domain.model.Location
import com.example.domain.repository.HistoryLocationRepository
import javax.inject.Inject

class SaveLocationUseCase @Inject constructor(private val repository : HistoryLocationRepository) {
    suspend operator fun invoke(location : Location) = repository.saveLocation(location)
}