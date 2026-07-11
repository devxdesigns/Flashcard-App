package com.example.flashcardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcardapp.data.AppContainer
import com.example.flashcardapp.screens.HomeScreen
import com.example.flashcardapp.ui.theme.FlashcardAppTheme
import com.example.flashcardapp.viewmodel.DeckViewModel
import com.example.flashcardapp.viewmodel.DeckViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContainer = AppContainer(applicationContext)

        enableEdgeToEdge()

        setContent {

            FlashcardAppTheme {

                val deckViewModel: DeckViewModel = viewModel(
                    factory = DeckViewModelFactory(appContainer.repository)
                )

                HomeScreen(
                    deckViewModel = deckViewModel
                )
            }
        }
    }
}