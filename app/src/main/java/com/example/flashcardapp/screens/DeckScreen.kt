package com.example.flashcardapp.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcardapp.data.Deck
import kotlinx.coroutines.launch

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

    BackHandler {
        onBack()
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
                                },
                                colors = MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                enabled = !deck.isLocked,
                                onClick = {
                                    if (!deck.isLocked) {
                                        studyCards = studyCards.shuffled()
                                        currentCardIndex = 0
                                        showAnswer = false
                                    }
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (deck.isLocked)
                                            "☑ Lock Card Order"
                                        else
                                            "☐ Lock Card Order",
                                        fontSize = 16.sp,
                                    )
                                },
                                colors = MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                onClick = {

                                    deck.isLocked = !deck.isLocked

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
                key(currentCardIndex) {
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
                }            }

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
