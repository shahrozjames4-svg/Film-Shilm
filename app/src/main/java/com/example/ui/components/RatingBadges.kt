package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CinemaBorderDark
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaGreen
import com.example.ui.theme.CinemaRed
import com.example.ui.theme.CinemaSurfaceDark
import com.example.ui.theme.CinemaSurfaceHighlight

@Composable
fun CompactRatingBadge(
    rating: Double,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(CinemaSurfaceDark.copy(alpha = 0.92f))
            .border(1.dp, Color(0x33FFB800), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = CinemaGold,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = String.format("%.1f", rating),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun FullRatingsRow(
    imdbRating: Double,
    voteCount: String,
    rottenTomatoesScore: Int,
    metascore: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.testTag("full_ratings_row"),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // IMDb Badge
        ImdbRatingBadge(
            rating = imdbRating,
            votes = voteCount,
            modifier = Modifier.weight(1f)
        )

        // Rotten Tomatoes Badge
        RottenTomatoesBadge(
            score = rottenTomatoesScore,
            modifier = Modifier.weight(1f)
        )

        // Metacritic Badge
        MetascoreBadge(
            score = metascore,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ImdbRatingBadge(
    rating: Double,
    votes: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(CinemaSurfaceHighlight)
            .border(1.dp, CinemaBorderDark, RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp, horizontal = 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "IMDb",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CinemaGold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = CinemaGold,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = String.format("%.1f", rating),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "/10",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = "$votes votes",
                fontSize = 10.sp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun RottenTomatoesBadge(
    score: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(CinemaSurfaceHighlight)
            .border(1.dp, CinemaBorderDark, RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp, horizontal = 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "Rotten Tomatoes",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "🍅 $score%",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (score >= 60) CinemaRed else Color.LightGray
            )
            Text(
                text = if (score >= 75) "Certified Fresh" else if (score >= 60) "Fresh" else "Rotten",
                fontSize = 10.sp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun MetascoreBadge(
    score: Int,
    modifier: Modifier = Modifier
) {
    val badgeColor = when {
        score >= 61 -> CinemaGreen
        score >= 40 -> CinemaGold
        else -> CinemaRed
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(CinemaSurfaceHighlight)
            .border(1.dp, CinemaBorderDark, RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp, horizontal = 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "Metacritic",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Box(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(badgeColor)
                    .padding(horizontal = 6.dp, vertical = 1.dp)
            ) {
                Text(
                    text = "$score",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            }
            Text(
                text = "Universal Acclaim",
                fontSize = 9.sp,
                color = Color.LightGray
            )
        }
    }
}
