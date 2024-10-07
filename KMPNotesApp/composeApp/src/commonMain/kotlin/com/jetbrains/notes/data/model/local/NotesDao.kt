package com.jetbrains.notes.data.model.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createNote(note:Note)

    @Query("UPDATE notes SET status=:status WHERE id=:id")
    suspend fun updateTaskStatus(id: Long,status: String)

    @Query("UPDATE notes SET subtasks=:subTask WHERE id=:id")
    suspend fun updateSubTask(id: Long, subTask: List<String>)

    @Query("UPDATE notes SET completedSubTasks=:subTask WHERE id=:id")
    suspend fun updateCompletedSubtask(id: Long, subTask: List<String>)

    @Query("UPDATE notes SET progress=:progression WHERE id=:id")
    suspend fun updateProgression(id: Long, progression: Float)




//    @Query("DELETE FROM notes WHERE id = :id")
//    suspend fun deleteSubtask(id: Long,subtasks: List<String>)

    @Query("Select * from notes where id=:id")
    suspend fun getNoteById(id: Long): Note?


    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Long)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>>

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%'")
    fun searchNotes(query: String): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE dateOfBirth = :dateOfBirth")
    fun getNotesByDateOfBirth(dateOfBirth: String): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE dateOfBirth = :dateOfBirth AND selectedTime >= :currentTime")
    fun getTasksForToday(dateOfBirth: String, currentTime: String): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE status LIKE '%' || :label || '%'")
    fun getLabelList(label:String):Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getTaskById(id: Long): Flow<Note?>

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
    val selectedTime:String,
    val priority:String,
    val labels: List<String>,
    val status: String,
    val subtasks: List<String>,
    val completedSubTasks: List<String> = emptyList(),
    val progress : Float= 0f,
    val assignees: List<String>
)
