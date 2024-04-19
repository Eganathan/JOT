package dev.eknath.jot.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.eknath.jot.data.local.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
