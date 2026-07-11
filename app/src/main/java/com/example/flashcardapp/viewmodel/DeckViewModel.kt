package com.example.flashcardapp.viewmodel

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcardapp.data.Deck
import com.example.flashcardapp.data.Flashcard
import com.example.flashcardapp.data.entity.DeckEntity
import com.example.flashcardapp.data.entity.FlashcardEntity
import com.example.flashcardapp.data.repository.DeckRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DeckViewModel(
    private val repository: DeckRepository
) : ViewModel() {

    val decks = repository.getDecks()
        .map { deckList ->

            deckList.map { deckWithCards ->

                Deck(
                    id = deckWithCards.deck.id,
                    name = deckWithCards.deck.name,
                    isFavorite = deckWithCards.deck.isFavorite,
                    isLocked = deckWithCards.deck.isLocked,
                    cards = deckWithCards.cards.map {

                        Flashcard(
                            id = it.id,
                            deckId = it.deckId,
                            question = it.question,
                            answer = it.answer
                        )

                    }.toMutableList().toMutableStateList()
                )

            }

        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addDeck(deck: Deck) {

        viewModelScope.launch {

            val deckEntity = DeckEntity(
                name = deck.name,
                isFavorite = deck.isFavorite,
                isLocked = deck.isLocked
            )

            val cards = deck.cards.map {

                FlashcardEntity(
                    deckId = 0,
                    question = it.question,
                    answer = it.answer
                )

            }

            repository.addDeckWithCards(
                deckEntity,
                cards
            )
        }
    }

    fun toggleFavorite(deck: Deck) {
        viewModelScope.launch {
            repository.updateDeck(
                DeckEntity(
                    id = deck.id,
                    name = deck.name,
                    isFavorite = !deck.isFavorite,
                    isLocked = deck.isLocked
                )
            )
        }
    }

    fun toggleLock(deck: Deck) {
        viewModelScope.launch {
            repository.updateDeck(
                DeckEntity(
                    id = deck.id,
                    name = deck.name,
                    isFavorite = deck.isFavorite,
                    isLocked = !deck.isLocked
                )
            )
        }
    }

    fun removeDeck(deck: Deck) {
        viewModelScope.launch {
            repository.deleteDeck(
                DeckEntity(
                    id = deck.id,
                    name = deck.name,
                    isFavorite = deck.isFavorite,
                    isLocked = deck.isLocked
                )
            )
        }
    }

    fun renameDeck(deck: Deck, newName: String) {
        viewModelScope.launch {
            repository.updateDeck(
                DeckEntity(
                    id = deck.id,
                    name = newName,
                    isFavorite = deck.isFavorite,
                    isLocked = deck.isLocked
                )
            )
        }
    }

    fun addCard(deck: Deck, card: Flashcard) {
        viewModelScope.launch {

            repository.addCard(
                FlashcardEntity(
                    deckId = deck.id,
                    question = card.question,
                    answer = card.answer
                )
            )
        }
    }

    fun editCard(
        card: Flashcard,
        question: String,
        answer: String
    ) {
        viewModelScope.launch {
            repository.updateCard(
                FlashcardEntity(
                    id = card.id,
                    deckId = card.deckId,
                    question = question,
                    answer = answer
                )
            )
        }

        // Update the UI immediately
        card.question = question
        card.answer = answer
    }

    fun deleteCard(
        deck: Deck,
        card: Flashcard
    ) {
        viewModelScope.launch {

            repository.deleteCard(
                FlashcardEntity(
                    id = card.id,
                    deckId = card.deckId,
                    question = card.question,
                    answer = card.answer
                )
            )
        }
    }
}