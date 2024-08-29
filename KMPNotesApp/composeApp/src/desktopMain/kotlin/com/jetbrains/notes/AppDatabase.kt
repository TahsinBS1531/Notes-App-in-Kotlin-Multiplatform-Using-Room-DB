package com.jetbrains.notes

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.jetbrains.notes.data.model.local.AppDatabase
import com.jetbrains.notes.data.model.local.DB_NAME
import kotlinx.coroutines.Dispatchers
import java.io.File

fun getDatabaseBuilder(): AppDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), DB_NAME)
    return Room.databaseBuilder<AppDatabase>(dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}