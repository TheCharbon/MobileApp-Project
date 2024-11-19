package com.example.mobileapp_project.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "finances")
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val expense: Boolean,
    val category: String,
    val description: String,
    val amount: Double,
    val timestamp: Date
)
