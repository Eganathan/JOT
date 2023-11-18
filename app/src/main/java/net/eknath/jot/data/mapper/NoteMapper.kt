package net.eknath.jot.data.mapper

import net.eknath.jot.data.local.entity.NoteEntity
import net.eknath.jot.domain.model.Note
import net.eknath.jot.toDateString


class NoteMapper {
    fun mapToDomain(entity: NoteEntity): Note {
        return Note(
            id = entity.id,
            title = entity.title,
            content = entity.content,
        )
    }

    fun mapToEntity(note: Note): NoteEntity {
        return NoteEntity(
            id = note.id,
            title = note.title,
            content = note.content
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
