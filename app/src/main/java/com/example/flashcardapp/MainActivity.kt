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
import androidx.compose.material3.MenuDefaults
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.snap
import androidx.compose.runtime.mutableIntStateOf

data class Flashcard(
    var question: String, var answer: String
)

data class Deck(
    var name: String, val cards: MutableList<Flashcard>, var isFavorite: Boolean = false
)

val sampleDecks = mutableStateListOf(

    Deck(
        name = "Biology", cards = mutableListOf(
            Flashcard(
                question = "Powerhouse of the cell?", answer = "Mitochondria"
            )
        )
    ),

    Deck(
        name = "Korean", cards = mutableListOf(
            Flashcard(
                question = "안녕하세요 means?", answer = "Hello"
            ), Flashcard(
                question = "안녕 means?", answer = "Hi"
            ), Flashcard(
                question = "하세요 means?", answer = "do it"
            )
        )
    )
)

val lightPastelColors = listOf(
    Color(0xFFD6CCFF), // Lavender
    Color(0xFFC8F7DC), // Mint
    Color(0xFFFFD6C9), // Peach
    Color(0xFFCDEBFF), // Sky
    Color(0xFFFFF1B8), // Butter
    Color(0xFFFFD6E7), // Pink
    Color(0xFFD7F4F2), // Aqua
    Color(0xFFE4DCCF)  // Beige
)

val darkPastelColors = listOf(
    Color(0xFF6D5AA8), // Deep Lavender
    Color(0xFF4F7A63), // Forest Green
    Color(0xFFC73E17), // Burnt Peach
    Color(0xFF487DAF), // Slate Blue
    Color(0xFFB6A040), // Olive Gold
    Color(0xFF8A5A73), // Dusty Rose
    Color(0xFF20B2A6), // Deep Aqua
    Color(0xFFBB1B52)  // Warm Taupe
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
            deck = selectedDeck!!, onBack = {
                selectedDeck = null
            })
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
                            "Create Deck", color = MaterialTheme.colorScheme.onPrimary
                        )
                    },

                    navigationIcon = {

                        TextButton(
                            onClick = {
                                showCreateScreen = false
                            }) {
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

                            sampleDecks.add(
                                Deck(
                                    name = deckName, cards = newCards.toMutableList()
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
                })
        }

        return
    }

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
                    modifier = Modifier.fillMaxWidth(), onClick = {

                        selectedDeckForMenu!!.isFavorite = !selectedDeckForMenu!!.isFavorite

                        showBottomSheet = false
                    }) {

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

                        selectedDeckForMenu!!.name = renameText

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

                        sampleDecks.remove(
                            selectedDeckForMenu
                        )

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckScreen(
    deck: Deck, onBack: () -> Unit
) {

    var currentCardIndex by remember {
        mutableIntStateOf(0)
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

    var isSliding by remember {
        mutableStateOf(false)
    }

    var dragOffset by remember {
        mutableFloatStateOf(0f)
    }

    var isDragging by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    val slideOffset = remember {
        Animatable(0f)
    }

    val screenWidthPx = LocalWindowInfo.current.containerSize.width.toFloat()

    val currentCard = studyCards[currentCardIndex]

    suspend fun animateCardTransition(direction: Int) {

        if ((direction == 1 && currentCardIndex >= studyCards.lastIndex) ||
            (direction == -1 && currentCardIndex <= 0)
        ) {
            return
        }

        isSliding = true

        val exitOffset = if (direction == 1) -screenWidthPx
        else screenWidthPx

        val enterOffset = if (direction == 1) screenWidthPx
        else -screenWidthPx

        // Start from wherever the user's finger left the card
        slideOffset.snapTo(slideOffset.value + dragOffset)
        dragOffset = 0f

        // Continue sliding off-screen
        slideOffset.animateTo(
            targetValue = exitOffset, animationSpec = tween(
                durationMillis = 220, easing = FastOutSlowInEasing
            )
        )
        // Change card
        currentCardIndex += direction
        showAnswer = false

        // Move new card off-screen
        slideOffset.snapTo(enterOffset)

        // Slide new card in
        slideOffset.animateTo(
            targetValue = 0f, animationSpec = tween(
                durationMillis = 280, easing = FastOutSlowInEasing
            )
        )

        isSliding = false
    }

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
                                })

                        Text(deck.name)
                    }
                }, actions = {

                    Box {

                        IconButton(
                            onClick = {
                                showMenu = true
                            }) {
                            Text(
                                text = "⋮", fontSize = 28.sp
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Shuffle Cards", fontSize = 16.sp
                                    )
                                }, colors = MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.onPrimary
                                ), onClick = {
                                    studyCards = studyCards.shuffled()
                                    currentCardIndex = 0
                                    showAnswer = false
                                    showMenu = false
                                })
                        }
                    }
                })
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .graphicsLayer {
                        translationX = slideOffset.value + dragOffset
                    }
                    .pointerInput(currentCardIndex) {

                        detectHorizontalDragGestures(

                            onHorizontalDrag = { _, dragAmount ->

                                if (!isSliding) {
                                    isDragging = true
                                    dragOffset += dragAmount
                                }

                            },

                            onDragEnd = {

                                scope.launch {

                                    when {

                                        dragOffset < -(screenWidthPx * 0.25f) && currentCardIndex < studyCards.lastIndex -> {

                                            animateCardTransition(1)
                                        }

                                        dragOffset > (screenWidthPx * 0.25f) && currentCardIndex > 0 -> {

                                            animateCardTransition(-1)
                                        }

                                        else -> {

                                            slideOffset.snapTo(dragOffset)

                                            dragOffset = 0f

                                            slideOffset.animateTo(
                                                targetValue = 0f, animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                    stiffness = Spring.StiffnessMedium
                                                )
                                            )
                                        }
                                    }
                                    isDragging = false
                                }
                            }
                        )
                    }) {
                StudyFlashcard(
                    currentCard = currentCard,
                    currentCardIndex = currentCardIndex,
                    totalCards = studyCards.size,
                    isFlipped = showAnswer,
                    onCardClick = {
                        if (!isDragging && !isSliding) {
                            showAnswer = !showAnswer
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = {
                        if (currentCardIndex > 0 && !isSliding) {
                            scope.launch {
                                animateCardTransition(-1)
                            }
                        }
                    }) {
                    Text(
                        text = "←", fontSize = 28.sp
                    )
                }

                Button(
                    onClick = {
                        if (currentCardIndex < studyCards.size - 1 && !isSliding) {
                            scope.launch {
                                animateCardTransition(1)
                            }
                        }
                    }) {
                    Text(
                        text = "→", fontSize = 28.sp
                    )
                }
            }
        }
    }

}

@Composable
fun StudyFlashcard(
    currentCard: Flashcard,
    currentCardIndex: Int,
    totalCards: Int,
    isFlipped: Boolean,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec =
            if (isFlipped)
                tween(400)
            else
                snap(),
        label = "CardFlip"
    )
    val density = LocalDensity.current

    val darkTheme = isSystemInDarkTheme()

    val cardColor = if (darkTheme) darkPastelColors[currentCardIndex % darkPastelColors.size]
    else lightPastelColors[currentCardIndex % lightPastelColors.size]

    Card(
        onClick = onCardClick,

        shape = RoundedCornerShape(32.dp),

        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),

        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density.density * 100f
            }) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    if (rotation > 90f) {
                        rotationY = 180f
                    }
                }) {

            Text(
                text = "${currentCardIndex + 1}/$totalCards",
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )

            Text(
                text = if (isFlipped) currentCard.answer
                else currentCard.question,

                fontSize = 28.sp,

                modifier = Modifier.align(Alignment.Center)
            )

            Text(
                text = if (isFlipped) "Tap to show question"
                else "Tap to show answer",

                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )
        }
    }
}