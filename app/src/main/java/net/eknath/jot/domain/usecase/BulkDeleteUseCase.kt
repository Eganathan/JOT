package net.eknath.jot.domain.usecase

import net.eknath.jot.domain.repository.NoteRepository

class BulkDeleteUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(notes: List<Long>) {
        noteRepository.deleteAll(notes)
    }
}