package com.example.flashcardapp.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import com.example.flashcardapp.darkPastelColors
import com.example.flashcardapp.data.Flashcard
//import com.example.flashcardapp.lightPastelColors

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
        animationSpec = tween(durationMillis = 400),
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
