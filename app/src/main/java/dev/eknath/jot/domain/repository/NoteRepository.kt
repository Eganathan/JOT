package dev.eknath.jot.domain.repository

import kotlinx.coroutines.flow.Flow
import dev.eknath.jot.domain.model.Note

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(noteId: Long): Note?
    fun searchNotes(searchQuery: String): Flow<List<Note>>
    suspend fun insert(note: Note): Long
    suspend fun update(note: Note)
    suspend fun delete(id: Long)
    suspend fun deleteAll(notes: List<Long>)
}