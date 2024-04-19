package dev.eknath.jot.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.eknath.jot.data.local.database.AppDatabase
import dev.eknath.jot.data.local.database.NoteDao
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DaoModule {
    @Provides
    @Singleton
    fun provideNotesDao(appDatabase: AppDatabase): NoteDao {
        return appDatabase.noteDao()
    }
}