package com.example.flashcardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.flashcardapp.ui.theme.FlashcardAppTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton

data class Flashcard(
    var question: String,
    var answer: String
)

data class Deck(
    var name: String,
    val cards: MutableList<Flashcard>,
    var isFavorite: Boolean = false
)

val sampleDecks = mutableStateListOf(

    Deck(
        name = "Biology",
        cards = mutableListOf(
            Flashcard(
                question = "Powerhouse of the cell?",
                answer = "Mitochondria"
            )
        )
    ),

    Deck(
        name = "Korean",
        cards = mutableListOf(
            Flashcard(
                question = "안녕하세요 means?",
                answer = "Hello"
            )
        )
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashcardAppTheme {
                HomeScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    var showCreateScreen by remember { mutableStateOf(false) }
    var selectedDeck by remember { mutableStateOf<Deck?>(null) }
    var selectedDeckForMenu by remember {
        mutableStateOf<Deck?>(null)
    }

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    var showRenameDialog by remember {
        mutableStateOf(false)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var renameText by remember {
        mutableStateOf("")
    }

    if (selectedDeck != null) {
        DeckScreen(
            deck = selectedDeck!!,
            onBack = {
                selectedDeck = null
            }
        )
        return
    }

    if (showCreateScreen) {

        var deckName by remember { mutableStateOf("") }
        var question by remember { mutableStateOf("") }
        var answer by remember { mutableStateOf("") }

        val newCards = remember {
            mutableStateListOf<Flashcard>()
        }

        var showEditDialog by remember {
            mutableStateOf(false)
        }

        var editingCard by remember {
            mutableStateOf<Flashcard?>(null)
        }

        var editQuestion by remember {
            mutableStateOf("")
        }

        var editAnswer by remember {
            mutableStateOf("")
        }

        Scaffold(

            topBar = {

                TopAppBar(

                    title = {
                        Text(
                            "Create Deck",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },

                    navigationIcon = {

                        TextButton(
                            onClick = {
                                showCreateScreen = false
                            }
                        ) {
                            Text(
                                text = "←",
                                fontSize = 30.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                OutlinedTextField(
                    value = deckName,
                    onValueChange = { deckName = it },
                    label = { Text(
                        text= "Deck Name",
                        color= MaterialTheme.colorScheme.secondary) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text(
                        text = "Question",
                        color= MaterialTheme.colorScheme.secondary) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text(
                        text = "Answer",
                        color= MaterialTheme.colorScheme.secondary) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {

                        if (
                            question.isNotBlank() &&
                            answer.isNotBlank()
                        ) {

                            newCards.add(
                                Flashcard(
                                    question = question,
                                    answer = answer
                                )
                            )

                            question = ""
                            answer = ""
                        }
                    }
                ) {
                    Text(
                        text= "Add Flashcard",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary)  //dark purple
                }

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                Text(
                    text = "Added Cards",
                    fontSize = 22.sp
                )

                newCards.forEach { card ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {

                                editingCard = card

                                editQuestion = card.question
                                editAnswer = card.answer

                                showEditDialog = true
                            }
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp),

                            horizontalArrangement =
                                Arrangement.SpaceBetween,

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Column(
                                modifier = Modifier.padding(start = 10.dp)
                            ) {

                                Text(
                                    text = "Q: ${card.question}"
                                )

                                Spacer(
                                    modifier = Modifier.height(2.dp)
                                )

                                Text(
                                    text = "A: ${card.answer}"
                                )
                            }

                            TextButton(
                                onClick = {
                                    newCards.remove(card)
                                }
                            ) {
                                Text(
                                    text = "X",
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {

                        if (
                            deckName.isNotBlank() &&
                            newCards.isNotEmpty()
                        ) {

                            sampleDecks.add(
                                Deck(
                                    name = deckName,
                                    cards = newCards.toMutableList()
                                )
                            )

                            showCreateScreen = false
                        }
                    }
                ) {
                    Text(
                        text= "Save Deck",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }

        if (showEditDialog && editingCard != null) {

            AlertDialog(

                onDismissRequest = {
                    showEditDialog = false
                },

                title = {
                    Text("Edit Flashcard")
                },

                text = {

                    Column {

                        OutlinedTextField(
                            value = editQuestion,
                            onValueChange = {
                                editQuestion = it
                            },
                            label = {
                                Text("Question")
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        OutlinedTextField(
                            value = editAnswer,
                            onValueChange = {
                                editAnswer = it
                            },
                            label = {
                                Text("Answer")
                            }
                        )
                    }
                },

                confirmButton = {

                    Button(
                        onClick = {

                            editingCard?.question = editQuestion
                            editingCard?.answer = editAnswer

                            showEditDialog = false
                        }
                    ) {
                        Text("Save")
                    }
                },

                dismissButton = {

                    Button(
                        onClick = {
                            showEditDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        return
    }

    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text(
                        "Flashcard Decks",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },

        floatingActionButton = {

            if (!showBottomSheet) {

                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        showCreateScreen = true
                    }
                ) {
                    Text(
                        text = "+",
                        fontSize = 40.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            sampleDecks
                .sortedByDescending { it.isFavorite }
                .forEach { deck ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .combinedClickable(

                                onClick = {
                                    selectedDeck = deck
                                },

                                onLongClick = {

                                    selectedDeckForMenu = deck
                                    renameText = deck.name
                                    showBottomSheet = true
                                }
                            )

                    ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text =
                                if (deck.isFavorite)
                                    " ⭐ ${deck.name}"
                                else
                                    deck.name
                        )
                    }
                }
            }
        }
    }

    if (showBottomSheet && selectedDeckForMenu != null) {

        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            }
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text =
                        if (selectedDeckForMenu!!.isFavorite)
                            "⭐ ${selectedDeckForMenu!!.name}"
                        else
                            selectedDeckForMenu!!.name,

                    fontSize = 24.sp
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        showRenameDialog = true
                    }
                ) {
                    Text("✏ Rename")
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {

                        selectedDeckForMenu!!.isFavorite =
                            !selectedDeckForMenu!!.isFavorite

                        showBottomSheet = false
                    }
                ) {

                    Text(
                        if (selectedDeckForMenu!!.isFavorite)
                            "⭐ Remove Favorite"
                        else
                            "⭐ Add Favorite"
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        showDeleteDialog = true
                    }
                ) {
                    Text("🗑 Delete")
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        showBottomSheet = false
                    }
                ) {
                    Text("✖ Cancel")
                }
            }
        }
    }

    if (showRenameDialog && selectedDeckForMenu != null) {

        AlertDialog(

            onDismissRequest = {
                showRenameDialog = false
            },

            title = {
                Text("Rename Deck")
            },

            text = {

                OutlinedTextField(
                    value = renameText,
                    onValueChange = {
                        renameText = it
                    }
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        selectedDeckForMenu!!.name =
                            renameText

                        showRenameDialog = false
                        showBottomSheet = false
                    }
                ) {
                    Text("Save")
                }
            },

            dismissButton = {

                Button(
                    onClick = {
                        showRenameDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDeleteDialog && selectedDeckForMenu != null) {

        AlertDialog(

            onDismissRequest = {
                showDeleteDialog = false
            },

            title = {
                Text("Delete Deck?")
            },

            text = {
                Text(
                    "Delete ${selectedDeckForMenu!!.name}?"
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        sampleDecks.remove(
                            selectedDeckForMenu
                        )

                        showDeleteDialog = false
                        showBottomSheet = false
                    }
                ) {
                    Text("Delete")
                }
            },

            dismissButton = {

                Button(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckScreen(
    deck: Deck,
    onBack: () -> Unit
) {

    var currentCardIndex by remember {
        mutableStateOf(0)
    }

    var showAnswer by remember {
        mutableStateOf(false)
    }

    var studyCards by remember {
        mutableStateOf(deck.cards.toList())
    }

    var showMenu by remember {
        mutableStateOf(false)
    }

    val currentCard = studyCards[currentCardIndex]

    Scaffold(

        topBar = {
            TopAppBar(

                title = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "←",
                            fontSize = 30.sp,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickable {
                                    onBack()
                                }
                        )

                        Text(deck.name)
                    }
                },
                actions = {

                    Box {

                        IconButton(
                            onClick = {
                                showMenu = true
                            }
                        ) {
                            Text(
                                text = "⋮",
                                fontSize = 28.sp
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = {
                                showMenu = false
                            }
                        ) {

                            DropdownMenuItem(

                                text = {
                                    Text("Shuffle Cards")
                                },

                                onClick = {

                                    studyCards = studyCards.shuffled()

                                    currentCardIndex = 0
                                    showAnswer = false

                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Card(
                onClick = {
                    showAnswer = !showAnswer
                },

                shape = RoundedCornerShape(32.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF64B5F6)
                ),

                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 5.dp)
            ) {

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {

                    Text(
                        text = "${currentCardIndex + 1}/${studyCards.size}",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    )

                    Text(
                        text =
                            if (showAnswer)
                                currentCard.answer
                            else
                                currentCard.question,

                        fontSize = 28.sp,

                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    )

                    Text(
                        text = "Tap card to flip",

                        modifier = Modifier
                            .align(
                                Alignment.BottomCenter
                            )
                            .padding(bottom = 16.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = {

                        if (currentCardIndex > 0) {
                            currentCardIndex--
                            showAnswer = false
                        }
                    }
                ) {
                    Text(
                        text = "←",
                        fontSize = 28.sp,)
                }

                Button(
                    onClick = {

                        if (
                            currentCardIndex < studyCards.size - 1
                        ) {
                            currentCardIndex++
                            showAnswer = false
                        }
                    }
                ) {
                    Text(text = "→",
                        fontSize = 28.sp)
                }
            }
        }
    }

}
