package com.example.flashcardapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "flashcards",
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["id"],
            childColumns = ["deckId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FlashcardEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val deckId: Int,

    val question: String,

    val answer: String
)