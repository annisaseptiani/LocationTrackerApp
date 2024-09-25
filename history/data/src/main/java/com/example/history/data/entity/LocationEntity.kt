package com.example.history.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude : Double,
    val longitude : Double,
    val timeStamp : Long,
    val isSynced : Boolean = false,
    val isOffline : Boolean = false)
