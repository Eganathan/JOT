package net.eknath.jot.domain.usecase

import net.eknath.jot.domain.model.Note
import net.eknath.jot.domain.repository.NoteRepository

class GetNoteUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(noteId: Long): Note? {
        return noteRepository.getNoteById(noteId)
    }
}