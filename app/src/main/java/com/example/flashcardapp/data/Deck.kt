package com.example.flashcardapp.data

data class Deck(
    var name: String,
    val cards: MutableList<Flashcard>,
    var isFavorite: Boolean = false,
    var isLocked: Boolean = false
)
