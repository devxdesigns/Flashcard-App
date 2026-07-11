package com.example.flashcardapp.data

data class Flashcard(
    var id: Int = 0,
    var deckId: Int = 0,
    var question: String,
    var answer: String
)
