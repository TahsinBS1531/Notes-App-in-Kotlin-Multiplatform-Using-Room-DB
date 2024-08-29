package com.jetbrains.notes.data.model.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNote(note:Note)

    @Query("Select * from notes where id=:id")
    suspend fun getNoteById(id: Long): Note?

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Long)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>>


}

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val content: String,
    val email: String,
    val dateOfBirth:String,
    val createdAt: Long= Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long?=null,
)