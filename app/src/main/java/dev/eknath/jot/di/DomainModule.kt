package dev.eknath.jot.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.eknath.jot.data.repository.NoteRepositoryImpl
import dev.eknath.jot.domain.usecase.GetAllNotesUseCase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DomainModule {
    @Provides
    @Singleton
    fun provideGetAllNotesUseCase(noteRepositoryImpl: NoteRepositoryImpl): GetAllNotesUseCase{
        return GetAllNotesUseCase(noteRepositoryImpl)
    }

}