package com.example.flashcardapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class DeckEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val isFavorite: Boolean,

    val isLocked: Boolean
)
