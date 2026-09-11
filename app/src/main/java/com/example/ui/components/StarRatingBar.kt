package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CinemaGold

@Composable
fun StarRatingBar(
    rating: Float, // 0 to 5
    onRatingChanged: ((Float) -> Unit)? = null,
    modifier: Modifier = Modifier,
    starSize: Dp = 24.dp,
    activeColor: Color = CinemaGold,
    inactiveColor: Color = Color(0xFF404040)
) {
    Row(
        modifier = modifier.testTag("star_rating_bar"),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (i in 1..5) {
            val isSelected = i <= rating
            val isHalf = !isSelected && (i - 0.5f) <= rating

            Icon(
                imageVector = if (isSelected || isHalf) Icons.Filled.Star else Icons.Outlined.StarBorder,
                contentDescription = "Rating $i stars",
                tint = if (isSelected || isHalf) activeColor else inactiveColor,
                modifier = Modifier
                    .size(starSize)
                    .then(
                        if (onRatingChanged != null) {
                            Modifier.clickable { onRatingChanged(i.toFloat()) }
                        } else Modifier
                    )
            )
        }
    }
}
