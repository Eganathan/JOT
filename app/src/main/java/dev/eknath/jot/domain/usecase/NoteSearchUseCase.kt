package dev.eknath.jot.domain.usecase

import kotlinx.coroutines.flow.Flow
import dev.eknath.jot.domain.model.Note
import dev.eknath.jot.domain.repository.NoteRepository
import javax.inject.Inject

class SearchNotesUseCase@Inject constructor(private val noteRepository: NoteRepository) {
    operator fun invoke(searchQuery: String): Flow<List<Note>> {
        return noteRepository.searchNotes(searchQuery)
    }
}
