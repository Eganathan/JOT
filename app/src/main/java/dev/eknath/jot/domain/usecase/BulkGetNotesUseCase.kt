package dev.eknath.jot.domain.usecase

import kotlinx.coroutines.flow.Flow
import dev.eknath.jot.domain.model.Note
import dev.eknath.jot.domain.repository.NoteRepository
import javax.inject.Inject

class GetAllNotesUseCase@Inject constructor(private val noteRepository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> {
        return noteRepository.getAllNotes()
    }
}