package com.example.flashcardapp.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.flashcardapp.data.Deck
import com.example.flashcardapp.data.DeckRepository
import com.example.flashcardapp.data.Flashcard

class DeckViewModel : ViewModel() {

    val decks: SnapshotStateList<Deck> = DeckRepository.getDecks()

    fun addDeck(deck: Deck) {
        DeckRepository.addDeck(deck)
    }
    fun removeDeck(deck: Deck) {
        DeckRepository.removeDeck(deck)
    }
    fun toggleFavorite(deck: Deck) {
        DeckRepository.toggleFavorite(deck)
    }

    fun toggleLock(deck: Deck) {
        DeckRepository.toggleLock(deck)
    }

    fun addCard(deck: Deck, card: Flashcard) {
        DeckRepository.addCard(deck, card)
    }

    fun editCard(
        card: Flashcard,
        question: String,
        answer: String
    ) {
        DeckRepository.editCard(card, question, answer)
    }

    fun deleteCard(deck: Deck, card: Flashcard) {
        DeckRepository.deleteCard(deck, card)
    }

    fun deleteSelectedCards(
        deck: Deck,
        selectedCards: List<Flashcard>
    ) {
        DeckRepository.deleteSelectedCards(deck, selectedCards)
    }
}