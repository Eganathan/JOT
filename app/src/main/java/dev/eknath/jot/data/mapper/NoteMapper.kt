package dev.eknath.jot.data.mapper

import dev.eknath.jot.data.local.entity.NoteEntity
import dev.eknath.jot.domain.model.Note
import dev.eknath.jot.getDate
import dev.eknath.jot.toLongTimestamp
import java.util.Date


class NoteMapper {
    fun mapToDomain(entity: NoteEntity): Note {
        return Note(
            id = entity.id,
            title = entity.title,
            content = entity.content,
            createdDate = entity.createdTimeStamp.getDate(),
            lastModifiedDate = entity.modifiedTimeStamp.getDate()
        )
    }

    fun mapToEntity(note: Note): NoteEntity {
        return NoteEntity(
            id = note.id,
            title = note.title,
            content = note.content,
            modifiedTimeStamp = note.lastModifiedDate.toLongTimestamp(),
            createdTimeStamp = note.createdDate.toLongTimestamp()
        )
    }

//    fun mapToDomain(dto: NoteDto): Note {
//        return Note(
//            id = dto.id,
//            title = dto.title,
//            content = dto.content
//        )
//    }
//
//    fun mapToDto(note: Note): NoteDto {
//        return NoteDto(
//            id = note.id,
//            title = note.title,
//            content = note.content
//        )
//    }
}
