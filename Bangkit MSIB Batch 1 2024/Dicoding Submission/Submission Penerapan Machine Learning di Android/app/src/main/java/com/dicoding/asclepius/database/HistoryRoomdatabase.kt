package com.dicoding.asclepius.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [History::class], version = 1)
abstract class HistoryRoomdatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryRoomdatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): HistoryRoomdatabase {
            if (INSTANCE == null) {
                synchronized(HistoryRoomdatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        HistoryRoomdatabase::class.java, "note_database")
                        .build()
                }
            }
            return INSTANCE as HistoryRoomdatabase
        }
    }
}