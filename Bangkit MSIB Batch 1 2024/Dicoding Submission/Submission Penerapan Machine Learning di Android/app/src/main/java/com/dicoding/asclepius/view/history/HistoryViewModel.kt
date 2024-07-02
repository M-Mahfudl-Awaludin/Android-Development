package com.dicoding.asclepius.view.history

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.repository.HistoryRepository

class HistoryViewModel(application: Application) : ViewModel() {
    private val mNoteRepository: HistoryRepository = HistoryRepository(application)
    fun getAllNotes(): LiveData<List<History>> = mNoteRepository.getAllNotes()

    fun delete(note: History) {
        mNoteRepository.delete(note)
    }
}