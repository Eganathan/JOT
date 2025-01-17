package dev.eknath.jot.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dev.eknath.jot.data.local.database.NoteDao
import dev.eknath.jot.data.mapper.NoteMapper
import dev.eknath.jot.domain.model.Note
import dev.eknath.jot.domain.repository.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val noteMapper: NoteMapper,
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map(noteMapper::mapToDomain)
        }
    }

    override suspend fun getNoteById(noteId: Long): Note? {
        val note = noteDao.getNoteById(noteId)
        return note?.let { noteMapper.mapToDomain(it) }
    }

    override fun searchNotes(searchQuery: String): Flow<List<Note>> {
        return noteDao.searchNotes(searchQuery = searchQuery).map { entities ->
            entities.map(noteMapper::mapToDomain)
        }
    }

    override suspend fun insert(note: Note): Long {
        val noteEntity = noteMapper.mapToEntity(note)
        return noteDao.insert(noteEntity)
    }

    override suspend fun update(note: Note) {
        val noteEntity = noteMapper.mapToEntity(note)
        noteDao.update(noteEntity.id,noteEntity.title,noteEntity.content,noteEntity.modifiedTimeStamp)
    }

    override suspend fun delete(id: Long) {
        noteDao.delete(id)
    }

    override suspend fun deleteAll(notes: List<Long>) {
        noteDao.deleteAllByIds(notes)
    }

    override suspend fun switchFavorite(id: Long, isFav: Boolean) {
        return noteDao.switchFav(id = id,isFav = isFav)
    }

}
