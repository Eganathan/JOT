package net.eknath.jot.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import net.eknath.jot.data.local.entity.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM jots")
     fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM jots WHERE id == :id")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Query("SELECT * FROM jots WHERE title LIKE '%' || :searchQuery || '%' OR content LIKE '%' || :searchQuery || '%'")
     fun searchNotes(searchQuery: String): Flow<List<NoteEntity>>

    @Insert
    suspend fun insert(note: NoteEntity): Long

    @Update
    suspend fun update(note: NoteEntity)

    @Query("DELETE FROM jots WHERE id IN (:id)")
    suspend fun delete(id: Long)

    @Query("DELETE FROM jots WHERE id IN (:noteIds)")
    suspend fun deleteAllByIds(noteIds: List<Long>)
}