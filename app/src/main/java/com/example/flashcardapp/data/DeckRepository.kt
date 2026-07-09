package com.example.flashcardapp.data

import androidx.compose.runtime.mutableStateListOf

object DeckRepository {

    private val decks = mutableStateListOf(

        Deck(
            name = "Biology",
            cards = mutableStateListOf(
                Flashcard(
                    question = "Powerhouse of the cell?",
                    answer = "Mitochondria"
                )
            )
        ),

        Deck(
            name = "Korean",
            cards = mutableStateListOf(
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

    fun toggleFavorite(deck: Deck) {
        deck.isFavorite = !deck.isFavorite
    }

    fun toggleLock(deck: Deck) {
        deck.isLocked = !deck.isLocked
    }

    fun addCard(deck: Deck, card: Flashcard) {
        deck.cards.add(card)
    }

    fun editCard(
        card: Flashcard,
        question: String,
        answer: String
    ) {
        card.question = question
        card.answer = answer
    }

    fun deleteCard(deck: Deck, card: Flashcard) {
        deck.cards.remove(card)
    }

    fun deleteSelectedCards(
        deck: Deck,
        selectedCards: List<Flashcard>
    ) {
        deck.cards.removeAll(selectedCards.toSet())
    }
}