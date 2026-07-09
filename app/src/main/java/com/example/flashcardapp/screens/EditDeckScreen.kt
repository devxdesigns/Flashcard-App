package com.example.flashcardapp.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcardapp.data.Deck
import com.example.flashcardapp.data.Flashcard
import com.example.flashcardapp.viewmodel.DeckViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDeckScreen(
    deck: Deck,
    deckViewModel: DeckViewModel,
    onBack: () -> Unit
) {
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

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var newQuestion by remember {
        mutableStateOf("")
    }

    var newAnswer by remember {
        mutableStateOf("")
    }

    var searchQuery by remember {
        mutableStateOf("")
    }
    var selectionMode by remember {
        mutableStateOf(false)
    }

    val selectedCards = remember {
        mutableStateListOf<Flashcard>()
    }

    val filteredCards = deck.cards.filter {

        it.question.contains(searchQuery, ignoreCase = true) ||

                it.answer.contains(searchQuery, ignoreCase = true)
    }

    BackHandler {
        if (selectionMode) {

            selectionMode = false
            selectedCards.clear()

        } else {

            onBack()
        }
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = if (selectionMode)
                            "${selectedCards.size} selected"
                        else
                            "Edit • ${deck.name}",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {

                    TextButton(
                        onClick = {

                            if (selectionMode) {

                                selectionMode = false
                                selectedCards.clear()

                            } else {

                                onBack()
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
                actions = {
                    if (selectionMode) {
                        TextButton(
                            onClick = {

                                deckViewModel.deleteSelectedCards(
                                    deck,
                                    selectedCards.toList()
                                )

                                selectionMode = false
                                selectedCards.clear()
                            }
                        ) {
                            Text(
                                text = "Delete",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    )    { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        )
        {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("🔍 Search cards...")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            filteredCards.forEach { card ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .combinedClickable(
                            onClick = {

                                if (selectionMode) {

                                    if (selectedCards.contains(card)) {

                                        selectedCards.remove(card)

                                        if (selectedCards.isEmpty()) {
                                            selectionMode = false
                                        }

                                    } else {

                                        selectedCards.add(card)
                                    }

                                } else {

                                    editingCard = card
                                    editQuestion = card.question
                                    editAnswer = card.answer
                                    showEditDialog = true
                                }
                            },

                            onLongClick = {

                                selectionMode = true

                                if (!selectedCards.contains(card))
                                    selectedCards.add(card)
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

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Q: ${card.question}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "A: ${card.answer}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (selectionMode) {

                            Checkbox(
                                checked = selectedCards.contains(card),
                                onCheckedChange = null
                            )
                        } else {
                            Text(
                                text = "✏",
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    newQuestion = ""
                    newAnswer = ""
                    showAddDialog = true
                }
            ) {
                Text("+ Add Card")
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

                    Spacer(modifier = Modifier.height(8.dp))

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

                        editingCard?.let { card ->
                            deckViewModel.editCard(
                                card,
                                editQuestion,
                                editAnswer
                            )
                        }
                        showEditDialog = false
                    }
                ) {
                    Text("Save")
                }
            },

            dismissButton = {

                Row {

                    Button(
                        onClick = {

                            editingCard?.let { card ->
                                deckViewModel.deleteCard(deck, card)
                            }

                            showEditDialog = false
                        }
                    ) {
                        Text("Delete")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            showEditDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            }
        )
    }

    if (showAddDialog) {

        AlertDialog(

            onDismissRequest = {
                showAddDialog = false
            },

            title = {
                Text("Add Flashcard")
            },

            text = {

                Column {

                    OutlinedTextField(
                        value = newQuestion,
                        onValueChange = {
                            newQuestion = it
                        },
                        label = {
                            Text("Question")
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newAnswer,
                        onValueChange = {
                            newAnswer = it
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

                        if (
                            newQuestion.isNotBlank() &&
                            newAnswer.isNotBlank()
                        ) {

                            deckViewModel.addCard(
                                deck,
                                Flashcard(
                                    question = newQuestion,
                                    answer = newAnswer
                                )
                            )

                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Add")
                }
            },

            dismissButton = {

                Button(
                    onClick = {
                        showAddDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
