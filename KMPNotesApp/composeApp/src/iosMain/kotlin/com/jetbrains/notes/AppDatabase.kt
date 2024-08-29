package com.jetbrains.notes

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.jetbrains.notes.data.model.local.AppDatabase
import com.jetbrains.notes.data.model.local.DB_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

fun getDatabaseBuilder(): AppDatabase {
    val dbFile = "${NSHomeDirectory()}/$DB_NAME"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile,
        factory = { AppDatabase::class.instantiateImpl() }
    ).setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}