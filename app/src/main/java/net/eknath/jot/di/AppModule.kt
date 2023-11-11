package net.eknath.jot.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.eknath.jot.data.local.database.AppDatabase
import net.eknath.jot.data.mapper.NoteMapper
import net.eknath.jot.data.repository.NoteRepositoryImpl
import net.eknath.jot.domain.repository.NoteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "jots_data_base"
        )
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(appDatabase: AppDatabase): NoteRepository {
        val noteDao = appDatabase.noteDao()
        return NoteRepositoryImpl(noteDao, NoteMapper())
    }
}