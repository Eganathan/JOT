package net.eknath.jot.domain.usecase

import kotlinx.coroutines.flow.Flow
import net.eknath.jot.domain.model.Note
import net.eknath.jot.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteUseCase@Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(noteId: Long): Note? {
        return noteRepository.getNoteById(noteId)
    }
}