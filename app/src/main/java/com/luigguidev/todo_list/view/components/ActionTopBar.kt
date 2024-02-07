package com.luigguidev.todo_list.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ActionTopBar(
    background: Color,
    cardColor: Color
){
    Card(
        modifier = Modifier
            .size(55.dp),
        shape = CutCornerShape(
            topStartPercent = 50,
            bottomStartPercent = 50,
        ),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .clip(shape = CircleShape)
                    .background(color = background)
                    .align(Alignment.Center)
            )
        }
    }
}