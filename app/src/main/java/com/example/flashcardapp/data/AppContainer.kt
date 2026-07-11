package com.example.flashcardapp.data

import android.content.Context
import androidx.room.Room
import com.example.flashcardapp.data.database.FlashcardDatabase
import com.example.flashcardapp.data.repository.DeckRepository

class AppContainer(context: Context) {

    private val database = Room.databaseBuilder(
        context,
        FlashcardDatabase::class.java,
        "flashcard_database"
    ).build()

    val repository = DeckRepository(
        database.deckDao(),
        database.flashcardDao()
    )
}