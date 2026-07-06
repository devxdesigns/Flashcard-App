package com.example.flashcardapp.data

import androidx.compose.runtime.mutableStateListOf

object DeckRepository {

    private val decks = mutableStateListOf(

        Deck(
            name = "Biology",
            cards = mutableListOf(
                Flashcard(
                    question = "Powerhouse of the cell?",
                    answer = "Mitochondria"
                )
            )
        ),

        Deck(
            name = "Korean",
            cards = mutableListOf(
                Flashcard(
                    question = "안녕하세요 means?",
                    answer = "Hello"
                ),
                Flashcard(
                    question = "안녕 means?",
                    answer = "Hi"
                ),
                Flashcard(
                    question = "하세요 means?",
                    answer = "do it"
                )
            )
        )
    )

    fun getDecks() = decks

    fun addDeck(deck: Deck) {
        decks.add(deck)
    }

    fun removeDeck(deck: Deck) {
        decks.remove(deck)
    }
}