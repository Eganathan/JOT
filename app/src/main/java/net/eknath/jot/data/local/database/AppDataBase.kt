package net.eknath.jot.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.eknath.jot.data.local.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    companion object {
        // Singleton prevents multiple instances of the database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // If the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "note_database"
                )
                    // Wipes and rebuilds instead of migrating
                    // if no Migration object.
                    // Migration is not part of this example.
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() //Todo need to remove this at the earlist
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
