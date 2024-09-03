package com.jetbrains.notes

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.jetbrains.notes.data.model.local.AppDatabase
import com.jetbrains.notes.data.model.local.DB_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

fun getDatabaseBuilder(ctx: Context): AppDatabase {
    val dbFile = ctx.getDatabasePath(DB_NAME)
    return Room.databaseBuilder(ctx, AppDatabase::class.java, dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryExecutor(Dispatchers.IO.asExecutor())
        .fallbackToDestructiveMigrationFrom(false,1, 2)
        .build()
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE notes ADD COLUMN selectedTime TEXT NOT NULL DEFAULT ''")

    }
}