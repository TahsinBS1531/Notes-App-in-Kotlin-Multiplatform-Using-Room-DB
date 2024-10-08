package com.jetbrains.notes.data.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Note::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(), DB {
    abstract fun getDao(): NotesDao
    override fun clearAllTables() {
    }
}

interface DB {
    fun clearAllTables()
}

internal const val DB_NAME = "notes.db"