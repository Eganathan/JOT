package dev.eknath.jot.domain.usecase

import dev.eknath.jot.domain.model.Note
import dev.eknath.jot.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteUseCase@Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(noteId: Long): Note? {
        return noteRepository.getNoteById(noteId)
    }
}