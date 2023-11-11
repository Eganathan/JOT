package net.eknath.jot.domain.usecase

import kotlinx.coroutines.flow.Flow
import net.eknath.jot.domain.model.Note
import net.eknath.jot.domain.repository.NoteRepository

class SearchNotesUseCase(private val noteRepository: NoteRepository) {
    operator fun invoke(searchQuery: String): Flow<List<Note>> {
        return noteRepository.searchNotes(searchQuery)
    }
}
