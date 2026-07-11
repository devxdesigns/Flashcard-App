package com.example.flashcardapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.flashcardapp.data.dao.DeckDao
import com.example.flashcardapp.data.dao.FlashcardDao
import com.example.flashcardapp.data.entity.DeckEntity
import com.example.flashcardapp.data.entity.FlashcardEntity

@Database(
    entities = [
        DeckEntity::class,
        FlashcardEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FlashcardDatabase : RoomDatabase() {

    abstract fun deckDao(): DeckDao

    abstract fun flashcardDao(): FlashcardDao
}