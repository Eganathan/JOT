package dev.eknath.jot.domain.usecase

import dev.eknath.jot.domain.repository.NoteRepository
import javax.inject.Inject


class DeleteNoteUseCase@Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(id: Long) {
        noteRepository.delete(id)
    }
}