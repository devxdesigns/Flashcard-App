package com.example.flashcardapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.flashcardapp.data.entity.DeckEntity
import com.example.flashcardapp.data.model.DeckWithCards
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {

    @Transaction
    @Query("SELECT * FROM decks")
    fun getDecksWithCards(): Flow<List<DeckWithCards>>

    @Insert
    suspend fun insertDeck(deck: DeckEntity): Long

    @Update
    suspend fun updateDeck(deck: DeckEntity)

    @Delete
    suspend fun deleteDeck(deck: DeckEntity)
}