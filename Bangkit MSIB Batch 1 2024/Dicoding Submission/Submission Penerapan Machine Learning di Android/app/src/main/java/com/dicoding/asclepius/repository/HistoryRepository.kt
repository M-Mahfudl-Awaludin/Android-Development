package com.dicoding.asclepius.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.database.HistoryDao
import com.dicoding.asclepius.database.HistoryRoomdatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryRepository(application: Application) {
    private val mNotesDao: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = HistoryRoomdatabase.getDatabase(application)
        mNotesDao = db.historyDao()
    }

    fun getAllNotes(): LiveData<List<History>> = mNotesDao.getAllNotes()

    fun insert(note: History) {
        executorService.execute { mNotesDao.insert(note) }
    }

    fun delete(note: History) {
        executorService.execute { mNotesDao.delete(note) }
    }

}