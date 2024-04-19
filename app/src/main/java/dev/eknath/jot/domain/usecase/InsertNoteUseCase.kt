package dev.eknath.jot.domain.usecase

import dev.eknath.jot.domain.model.Note
import dev.eknath.jot.domain.repository.NoteRepository
import javax.inject.Inject

class AddNoteUseCase@Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(note: Note):Long {
        return noteRepository.insert(note)
    }
}