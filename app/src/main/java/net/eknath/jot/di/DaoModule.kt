package net.eknath.jot.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.eknath.jot.data.local.database.AppDatabase
import net.eknath.jot.data.local.database.NoteDao
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