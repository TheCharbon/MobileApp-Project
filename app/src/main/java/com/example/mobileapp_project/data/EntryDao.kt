package com.example.mobileapp_project.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface EntryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entry: Entry)

    @Query("SELECT * from finances WHERE strftime('%Y-%m', timestamp / 1000, 'unixepoch') = :month")
    fun getItem(month: String): Flow<List<Entry>>
}