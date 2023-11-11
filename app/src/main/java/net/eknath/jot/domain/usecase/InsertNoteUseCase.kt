package net.eknath.jot.domain.usecase

import net.eknath.jot.domain.model.Note
import net.eknath.jot.domain.repository.NoteRepository
import javax.inject.Inject

class AddNoteUseCase@Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(note: Note):Long {
        return noteRepository.insert(note)
    }
}