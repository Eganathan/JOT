package net.eknath.jot.domain.model

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
)