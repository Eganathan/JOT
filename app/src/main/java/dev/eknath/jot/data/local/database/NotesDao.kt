package dev.eknath.jot.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import dev.eknath.jot.data.local.entity.NoteEntity
import java.util.Date

@Dao
interface NoteDao {
    @Query("SELECT * FROM jots ORDER BY modifiedTimeStamp DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM jots WHERE id == :id")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Query("SELECT * FROM jots WHERE title LIKE '%' || :searchQuery || '%' OR content LIKE '%' || :searchQuery || '%'")
    fun searchNotes(searchQuery: String): Flow<List<NoteEntity>>

    @Insert
    suspend fun insert(note: NoteEntity): Long

    @Update
    suspend fun update(note: NoteEntity)

    @Transaction
    suspend fun switchFav(id: Long, isFav: Boolean) {
        val task = getNoteById(id) ?: return
        update(task.copy(isFav = isFav))
    }

    @Query("DELETE FROM jots WHERE id IN (:id)")
    suspend fun delete(id: Long)

    @Query("DELETE FROM jots WHERE id IN (:noteIds)")
    suspend fun deleteAllByIds(noteIds: List<Long>)

    // Only update title, content, modifiedTimeStamp. Created
    @Query("UPDATE jots SET title = :title, content = :content, modifiedTimeStamp = :modifiedTimeStamp WHERE id =:id")
    suspend fun update(id: Long, title: String, content: String, modifiedTimeStamp: Long)
}