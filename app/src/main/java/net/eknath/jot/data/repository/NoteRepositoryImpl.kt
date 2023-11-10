package net.eknath.jot.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.eknath.jot.data.local.database.NoteDao
import net.eknath.jot.data.mapper.NoteMapper
import net.eknath.jot.domain.model.Note
import net.eknath.jot.domain.repository.NoteRepository

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val noteMapper: NoteMapper,
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map(noteMapper::mapToDomain)
        }
    }

    override fun getNoteById(noteId: Long): Note? {
        val note =  noteDao.getNoteById(noteId)
        return note?.let { noteMapper.mapToDomain(it) }
    }

    override suspend fun insert(note: Note): Long {
        val noteEntity = noteMapper.mapToEntity(note)
        return  noteDao.insert(noteEntity)
    }

    override suspend fun update(note: Note) {
        val noteEntity = noteMapper.mapToEntity(note)
        noteDao.update(noteEntity)
    }

    override suspend fun delete(note: Note) {
        val noteEntity = noteMapper.mapToEntity(note)
        noteDao.delete(noteEntity)
    }

}
