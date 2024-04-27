package dev.eknath.jot.domain.usecase

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.eknath.jot.domain.model.Note
import dev.eknath.jot.domain.repository.NoteRepository
import javax.inject.Inject

@InstallIn(SingletonComponent::class)
@Module
class SwitchFavUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(id: Long, isFav: Boolean) {
         noteRepository.switchFavorite(id = id, isFav = isFav)
    }
}