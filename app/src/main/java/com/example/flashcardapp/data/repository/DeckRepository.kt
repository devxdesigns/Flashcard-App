package com.example.flashcardapp.data.repository

import com.example.flashcardapp.data.dao.DeckDao
import com.example.flashcardapp.data.dao.FlashcardDao
import com.example.flashcardapp.data.entity.DeckEntity
import com.example.flashcardapp.data.entity.FlashcardEntity
import com.example.flashcardapp.data.model.DeckWithCards
import kotlinx.coroutines.flow.Flow

class DeckRepository(
    private val deckDao: DeckDao,
    private val flashcardDao: FlashcardDao
) {

    fun getDecks(): Flow<List<DeckWithCards>> =
        deckDao.getDecksWithCards()

    suspend fun addDeckWithCards(
        deck: DeckEntity,
        cards: List<FlashcardEntity>
    ) {
        val deckId = deckDao.insertDeck(deck).toInt()

        cards.forEach {
            flashcardDao.insertCard(
                it.copy(deckId = deckId)
            )
        }
    }

    suspend fun updateDeck(deck: DeckEntity) =
        deckDao.updateDeck(deck)

    suspend fun deleteDeck(deck: DeckEntity) =
        deckDao.deleteDeck(deck)

    suspend fun addCard(card: FlashcardEntity) =
        flashcardDao.insertCard(card)

    suspend fun updateCard(card: FlashcardEntity) =
        flashcardDao.updateCard(card)

    suspend fun deleteCard(card: FlashcardEntity) =
        flashcardDao.deleteCard(card)
}