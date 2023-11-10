package net.eknath.jot.domain.repository

import kotlinx.coroutines.flow.Flow
import net.eknath.jot.domain.model.Note

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(noteId: Long): Note?
    suspend fun insert(note: Note)
    suspend fun update(note: Note)
    suspend fun delete(note: Note)
//    suspend fun deleteAll(notes: List<Note>)
}