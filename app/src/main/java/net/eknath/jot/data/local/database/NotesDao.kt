package net.eknath.jot.data.local.database

import androidx.room.Dao
import androidx.room.Delete
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
    fun getNoteById(id: Long): NoteEntity?

    @Insert
    suspend fun insert(note: NoteEntity): Long

    @Update
    suspend fun update(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("DELETE FROM jots WHERE id IN (:noteIds)")
    suspend fun deleteAllByIds(noteIds: List<Long>)
}