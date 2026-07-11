package com.example.flashcardapp.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.flashcardapp.data.entity.DeckEntity
import com.example.flashcardapp.data.entity.FlashcardEntity

data class DeckWithCards(

    @Embedded
    val deck: DeckEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "deckId"
    )
    val cards: List<FlashcardEntity>
)