package net.eknath.jot.domain.usecase

import net.eknath.jot.domain.repository.NoteRepository
import javax.inject.Inject

class BulkDeleteUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(notes: List<Long>) {
        noteRepository.deleteAll(notes)
    }
}