package com.example.flashcardapp.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class Deck(
    var name: String,
    val cards: SnapshotStateList<Flashcard> = mutableStateListOf(),
    var isFavorite: Boolean = false,
    var isLocked: Boolean = false
)