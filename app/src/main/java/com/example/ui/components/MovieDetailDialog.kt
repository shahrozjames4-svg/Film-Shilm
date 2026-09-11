package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.Movie
import com.example.data.model.WatchlistEntry
import com.example.data.model.WatchlistStatus
import com.example.ui.theme.CinemaBorderDark
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaGreen
import com.example.ui.theme.CinemaRed
import com.example.ui.theme.CinemaSurfaceDark
import com.example.ui.theme.CinemaSurfaceElevated
import com.example.ui.theme.CinemaSurfaceHighlight

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieDetailDialog(
    movie: Movie,
    watchlistEntry: WatchlistEntry?,
    onDismiss: () -> Unit,
    onToggleWatchlist: () -> Unit,
    onToggleWatched: () -> Unit,
    onSaveReview: (Float, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showReviewEditor by remember { mutableStateOf(false) }
    var currentRating by remember(watchlistEntry) {
        mutableFloatStateOf(watchlistEntry?.userRating ?: 0f)
    }
    var currentNote by remember(watchlistEntry) {
        mutableStateOf(watchlistEntry?.userNote ?: "")
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, CinemaBorderDark, RoundedCornerShape(20.dp))
                .testTag("movie_detail_dialog"),
            color = CinemaSurfaceDark
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with Backdrop & Poster
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(movie.posterUrl)
                            .crossfade(true)
                            .error(R.drawable.img_cinema_hero)
                            .placeholder(R.drawable.img_cinema_hero)
                            .build(),
                        contentDescription = "Poster backdrop",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x99000000),
                                        Color(0x33000000),
                                        CinemaSurfaceDark
                                    )
                                )
                            )
                    )

                    // Close button top right
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .clip(CircleShape)
                            .background(Color(0xAA000000))
                            .testTag("close_detail_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }

                    // Content overlay at bottom of header
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = movie.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )

                        if (movie.tagline.isNotBlank()) {
                            Text(
                                text = "“${movie.tagline}”",
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                color = CinemaGold,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${movie.year}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(text = "•", color = Color.Gray)
                            Text(
                                text = movie.runtime,
                                fontSize = 13.sp,
                                color = Color.LightGray
                            )
                            Text(text = "•", color = Color.Gray)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF222222))
                                    .border(0.5.dp, CinemaBorderDark, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = movie.language,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CinemaGold
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF222222))
                                    .border(0.5.dp, CinemaBorderDark, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = movie.ageRating,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Body content
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Genres
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        movie.genres.forEach { genre ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF202020))
                                    .border(1.dp, CinemaBorderDark, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = genre,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = CinemaGold
                                )
                            }
                        }
                    }

                    // Ratings Breakdown
                    Text(
                        text = "CRITIC & AUDIENCE RATINGS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )

                    FullRatingsRow(
                        imdbRating = movie.rating,
                        voteCount = movie.voteCount,
                        rottenTomatoesScore = movie.rottenTomatoesScore,
                        metascore = movie.metascore,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Action Buttons (Watchlist & Watched)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val isInWatchlist = watchlistEntry != null
                        Button(
                            onClick = onToggleWatchlist,
                            modifier = Modifier
                                .weight(1.2f)
                                .height(46.dp)
                                .testTag("detail_watchlist_toggle_btn"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isInWatchlist) CinemaGold else Color(0xFF242424),
                                contentColor = if (isInWatchlist) Color.Black else Color.White
                            ),
                            border = if (!isInWatchlist) androidx.compose.foundation.BorderStroke(1.dp, CinemaBorderDark) else null
                        ) {
                            Icon(
                                imageVector = if (isInWatchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isInWatchlist) "In Watchlist" else "Add to Watchlist",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        if (isInWatchlist) {
                            val isWatched = watchlistEntry?.status == WatchlistStatus.WATCHED.name
                            OutlinedButton(
                                onClick = onToggleWatched,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("detail_watched_toggle_btn"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = if (isWatched) CinemaGreen else Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (isWatched) CinemaGreen else Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isWatched) "Watched" else "Mark Watched",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // User Rating & Notes Section (if in watchlist)
                    if (watchlistEntry != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = CinemaSurfaceElevated)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "YOUR RATING & NOTES",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CinemaGold,
                                        letterSpacing = 0.5.sp
                                    )
                                    IconButton(
                                        onClick = { showReviewEditor = !showReviewEditor },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit review",
                                            tint = Color.LightGray,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    StarRatingBar(
                                        rating = currentRating,
                                        onRatingChanged = { newRating ->
                                            currentRating = newRating
                                            onSaveReview(newRating, currentNote)
                                        },
                                        starSize = 22.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (currentRating > 0) "${currentRating.toInt()} / 5 Stars" else "Tap to rate",
                                        fontSize = 12.sp,
                                        color = Color.LightGray
                                    )
                                }

                                if (currentNote.isNotBlank() && !showReviewEditor) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "“$currentNote”",
                                        fontSize = 12.sp,
                                        fontStyle = FontStyle.Italic,
                                        color = Color(0xFFD4D4D8)
                                    )
                                }

                                if (showReviewEditor) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = currentNote,
                                        onValueChange = { currentNote = it },
                                        placeholder = { Text("Add personal thoughts, favorite scenes...", fontSize = 12.sp) },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = CinemaGold,
                                            unfocusedBorderColor = CinemaBorderDark,
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White,
                                            focusedContainerColor = Color(0xFF161616),
                                            unfocusedContainerColor = Color(0xFF161616)
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        maxLines = 3
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            onSaveReview(currentRating, currentNote)
                                            showReviewEditor = false
                                        },
                                        modifier = Modifier.align(Alignment.End),
                                        colors = ButtonDefaults.buttonColors(containerColor = CinemaGold),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Save Note", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = CinemaBorderDark)

                    // Synopsis
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "STORYLINE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = movie.synopsis,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = Color(0xFFE4E4E7)
                        )
                    }

                    // Cast & Crew
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "DIRECTOR & CAST",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            letterSpacing = 1.sp
                        )

                        Row {
                            Text(
                                text = "Director: ",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = movie.director,
                                fontSize = 13.sp,
                                color = CinemaGold
                            )
                        }

                        Row {
                            Text(
                                text = "Language: ",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = movie.language,
                                fontSize = 13.sp,
                                color = CinemaGold
                            )
                        }

                        Text(
                            text = "Starring:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            movie.cast.forEach { actor ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF202020))
                                        .border(0.5.dp, CinemaBorderDark, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = actor,
                                        fontSize = 12.sp,
                                        color = Color.LightGray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
