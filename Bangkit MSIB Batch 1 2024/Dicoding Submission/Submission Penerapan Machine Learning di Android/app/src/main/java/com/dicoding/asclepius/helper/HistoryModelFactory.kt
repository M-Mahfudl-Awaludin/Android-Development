package com.dicoding.asclepius.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.view.history.HistoryViewModel

class HistoryModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: HistoryModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): HistoryModelFactory {
            if (INSTANCE == null) {
                synchronized(HistoryModelFactory::class.java) {
                    INSTANCE = HistoryModelFactory(application)
                }
            }
            return INSTANCE as HistoryModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}