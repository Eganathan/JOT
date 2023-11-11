package net.eknath.jot.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.eknath.jot.data.mapper.NoteMapper
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MapperModule {
    @Provides
    @Singleton
    fun provideNoteMapper(): NoteMapper {
        return NoteMapper()
    }

}