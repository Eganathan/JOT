package dev.eknath.jot.domain.usecase

import dev.eknath.jot.domain.model.Note
import dev.eknath.jot.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase@Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(note: Note) {
        noteRepository.update(note)
    }
}