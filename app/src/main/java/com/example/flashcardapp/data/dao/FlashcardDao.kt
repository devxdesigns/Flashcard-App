package com.example.flashcardapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.flashcardapp.data.entity.FlashcardEntity

@Dao
interface FlashcardDao {

    @Insert
    suspend fun insertCard(card: FlashcardEntity)

    @Update
    suspend fun updateCard(card: FlashcardEntity)

    @Delete
    suspend fun deleteCard(card: FlashcardEntity)

}