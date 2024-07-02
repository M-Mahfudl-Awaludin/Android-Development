package com.dicoding.asclepius.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: History)

    @Update
    fun update(note: History)

    @Delete
    fun delete(note: History)

    @Query("SELECT * from History ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<History>>
}