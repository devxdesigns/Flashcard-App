package com.example.flashcardapp.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcardapp.data.Deck
import com.example.flashcardapp.data.Flashcard
import com.example.flashcardapp.viewmodel.DeckViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    deckViewModel: DeckViewModel
) {
    val sampleDecks by deckViewModel.decks.collectAsState()

    var showCreateScreen by remember { mutableStateOf(false) }
    var selectedDeck by remember { mutableStateOf<Deck?>(null) }
    var selectedDeckForMenu by remember {
        mutableStateOf<Deck?>(null)
    }
    var showEditDeckScreen by remember {
        mutableStateOf(false)
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

    if (showEditDeckScreen && selectedDeckForMenu != null) {

        EditDeckScreen(
            deck = selectedDeckForMenu!!,
            deckViewModel = deckViewModel,
            onBack = {
                showEditDeckScreen = false
            }
        )

        return
    }

    if (selectedDeck != null) {
        DeckScreen(
            deck = selectedDeck!!,
            deckViewModel = deckViewModel,
            onBack = {
                selectedDeck = null
            })
        return
    }
    //CREATE DECK SCREEN

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

        var showExitDialog by remember {
            mutableStateOf(false)
        }

        val hasUnsavedChanges =
            deckName.isNotBlank() ||
                    question.isNotBlank() ||
                    answer.isNotBlank() ||
                    newCards.isNotEmpty()

        BackHandler {
            if (hasUnsavedChanges) {
                showExitDialog = true
            } else {
                showCreateScreen = false
            }
        }

        Scaffold(

            topBar = {

                TopAppBar(

                    title = {
                        Text(
                            "Create Deck", color = MaterialTheme.colorScheme.onPrimary
                        )
                    },

                    navigationIcon = {

                        TextButton(
                            onClick = {
                                if (hasUnsavedChanges) {
                                    showExitDialog = true
                                } else {
                                    showCreateScreen = false
                                }
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
                    value = deckName, onValueChange = { deckName = it }, label = {
                        Text(
                            text = "Deck Name", color = MaterialTheme.colorScheme.secondary
                        )
                    }, modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = question, onValueChange = { question = it }, label = {
                        Text(
                            text = "Question", color = MaterialTheme.colorScheme.secondary
                        )
                    }, modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = answer, onValueChange = { answer = it }, label = {
                        Text(
                            text = "Answer", color = MaterialTheme.colorScheme.secondary
                        )
                    }, modifier = Modifier.fillMaxWidth()
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

                        if (question.isNotBlank() && answer.isNotBlank()) {

                            newCards.add(
                                Flashcard(
                                    question = question, answer = answer
                                )
                            )

                            question = ""
                            answer = ""
                        }
                    }) {
                    Text(
                        text = "Add Flashcard",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )  //dark purple
                }

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                Text(
                    text = "Added Cards", fontSize = 22.sp
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
                            }) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp),

                            horizontalArrangement = Arrangement.SpaceBetween,

                            verticalAlignment = Alignment.CenterVertically
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
                                }) {
                                Text(
                                    text = "X", fontSize = 14.sp
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

                        if (deckName.isNotBlank() && newCards.isNotEmpty()) {

                            deckViewModel.addDeck(
                                Deck(
                                    name = deckName, cards = newCards.toMutableStateList()
                                )
                            )

                            showCreateScreen = false
                        }
                    }) {
                    Text(
                        text = "Save Deck",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
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

                        OutlinedTextField(value = editQuestion, onValueChange = {
                            editQuestion = it
                        }, label = {
                            Text("Question")
                        })

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        OutlinedTextField(value = editAnswer, onValueChange = {
                            editAnswer = it
                        }, label = {
                            Text("Answer")
                        })
                    }
                },

                confirmButton = {

                    Button(
                        onClick = {

                            editingCard?.question = editQuestion
                            editingCard?.answer = editAnswer

                            showEditDialog = false
                        }) {
                        Text("Save")
                    }
                },

                dismissButton = {

                    Button(
                        onClick = {
                            showEditDialog = false
                        }) {
                        Text("Cancel")
                    }
                }
            )
        }
        if (showExitDialog) {

            AlertDialog(

                onDismissRequest = {
                    showExitDialog = false
                },

                title = {
                    Text("Discard changes?")
                },

                text = {
                    Text("Do you want to exit without saving this deck?")
                },

                confirmButton = {

                    Button(
                        onClick = {
                            showExitDialog = false
                            showCreateScreen = false
                        }
                    ) {
                        Text("Exit")
                    }
                },

                dismissButton = {

                    Button(
                        onClick = {
                            showExitDialog = false
                        }
                    ) {
                        Text("Stay")
                    }
                }
            )
        }
        return
    }

    //HOME SCREEN
    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text(
                        "Flashcard Decks", color = MaterialTheme.colorScheme.onPrimary
                    )
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },

        floatingActionButton = {

            if (!showBottomSheet) {

                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary, onClick = {
                        showCreateScreen = true
                    }) {
                    Text(
                        text = "+", fontSize = 40.sp, color = MaterialTheme.colorScheme.onPrimary
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

            sampleDecks.sortedByDescending { it.isFavorite }.forEach { deck ->

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
                            })

                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = if (deck.isFavorite) " ⭐ ${deck.name}"
                            else deck.name
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
            }) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = if (selectedDeckForMenu!!.isFavorite) "⭐ ${selectedDeckForMenu!!.name}"
                    else selectedDeckForMenu!!.name,

                    fontSize = 24.sp
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(), onClick = {
                        showRenameDialog = true
                    }) {
                    Text("✏ Rename")
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        showBottomSheet = false
                        showEditDeckScreen = true
                    }
                ) {
                    Text("📝 Edit Cards")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {

                        selectedDeckForMenu?.let { deck ->
                            deckViewModel.toggleFavorite(deck)
                        }

                        showBottomSheet = false
                    }
                ) {

                    Text(
                        if (selectedDeckForMenu!!.isFavorite) "⭐ Remove Favorite"
                        else "⭐ Add Favorite"
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(), onClick = {
                        showDeleteDialog = true
                    }) {
                    Text("🗑 Delete")
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(), onClick = {
                        showBottomSheet = false
                    }) {
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
                    value = renameText, onValueChange = {
                        renameText = it
                    })
            },

            confirmButton = {

                Button(
                    onClick = {

                        deckViewModel.renameDeck(
                            selectedDeckForMenu!!,
                            renameText
                        )

                        showRenameDialog = false
                        showBottomSheet = false
                    }) {
                    Text("Save")
                }
            },

            dismissButton = {

                Button(
                    onClick = {
                        showRenameDialog = false
                    }) {
                    Text("Cancel")
                }
            })
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

                        selectedDeckForMenu?.let { deck ->
                            deckViewModel.removeDeck(deck)
                        }

                        showDeleteDialog = false
                        showBottomSheet = false
                    }) {
                    Text("Delete")
                }
            },

            dismissButton = {

                Button(
                    onClick = {
                        showDeleteDialog = false
                    }) {
                    Text("Cancel")
                }
            })
    }
}

