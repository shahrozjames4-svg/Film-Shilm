package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.WatchlistEntry
import com.example.data.model.WatchlistStatus
import com.example.ui.WatchlistFilter
import com.example.ui.components.CompactRatingBadge
import com.example.ui.components.StarRatingBar
import com.example.ui.theme.CinemaBorderDark
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaGreen
import com.example.ui.theme.CinemaObsidian
import com.example.ui.theme.CinemaSurfaceDark
import com.example.ui.theme.CinemaSurfaceElevated

@Composable
fun WatchlistScreen(
    allWatchlist: List<WatchlistEntry>,
    filteredWatchlist: List<WatchlistEntry>,
    currentFilter: WatchlistFilter,
    onFilterChanged: (WatchlistFilter) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onEntryClick: (String) -> Unit,
    onToggleWatched: (WatchlistEntry) -> Unit,
    onUpdateRating: (String, Float, String) -> Unit,
    onDeleteEntry: (String, String) -> Unit,
    onExploreClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalSaved = allWatchlist.size
    val totalWatched = allWatchlist.count { it.status == WatchlistStatus.WATCHED.name }
    val totalWantToWatch = totalSaved - totalWatched

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CinemaObsidian)
            .testTag("watchlist_list"),
        contentPadding = PaddingValues(bottom = 96.dp, start = 14.dp, end = 14.dp, top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Top Stats & Header Banner
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                Text(
                    text = "MY WATCHLISTS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CinemaGold,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "Saved Movies & Reviews",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Stats Dashboard Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        title = "SAVED",
                        count = "$totalSaved",
                        subtitle = "Total Films",
                        color = CinemaGold,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "WATCHED",
                        count = "$totalWatched",
                        subtitle = "Completed",
                        color = CinemaGreen,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "QUEUED",
                        count = "$totalWantToWatch",
                        subtitle = "To Watch",
                        color = Color(0xFF38BDF8),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Watchlist Search
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    placeholder = {
                        Text("Search in your saved movies...", fontSize = 13.sp, color = Color.Gray)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { onSearchQueryChanged("") },
                                modifier = Modifier.testTag("clear_watchlist_search")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = Color.LightGray
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("watchlist_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CinemaGold,
                        unfocusedBorderColor = CinemaBorderDark,
                        focusedContainerColor = CinemaSurfaceDark,
                        unfocusedContainerColor = CinemaSurfaceDark,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Filter Chips (All, Want to Watch, Watched)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WatchlistFilter.values().forEach { filter ->
                        val count = when (filter) {
                            WatchlistFilter.ALL -> totalSaved
                            WatchlistFilter.WANT_TO_WATCH -> totalWantToWatch
                            WatchlistFilter.WATCHED -> totalWatched
                        }
                        val isSelected = currentFilter == filter

                        FilterChip(
                            selected = isSelected,
                            onClick = { onFilterChanged(filter) },
                            label = {
                                Text(
                                    text = "${filter.label} ($count)",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CinemaGold,
                                selectedLabelColor = Color.Black,
                                containerColor = CinemaSurfaceElevated,
                                labelColor = Color.LightGray
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) CinemaGold else CinemaBorderDark
                            ),
                            modifier = Modifier.testTag("watchlist_filter_${filter.name}")
                        )
                    }
                }
            }
        }

        // Empty state
        if (filteredWatchlist.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp)
                        .testTag("empty_watchlist_state"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(CinemaSurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = CinemaGold,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (totalSaved == 0) "Your Watchlist is Empty" else "No matching movies found",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (totalSaved == 0)
                            "Browse our featured movie catalog, tap the bookmark icon on any film, and build your personalized cinema queue."
                        else "No saved films match the current filter or search criteria.",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 28.dp)
                    )

                    if (totalSaved == 0) {
                        Spacer(modifier = Modifier.height(18.dp))
                        Button(
                            onClick = onExploreClicked,
                            colors = ButtonDefaults.buttonColors(containerColor = CinemaGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("explore_cta_btn")
                        ) {
                            Text(
                                text = "Discover Movies",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else {
            items(filteredWatchlist, key = { it.movieId }) { entry ->
                WatchlistItemCard(
                    entry = entry,
                    onItemClick = { onEntryClick(entry.movieId) },
                    onToggleWatched = { onToggleWatched(entry) },
                    onUpdateRating = { rating, note ->
                        onUpdateRating(entry.movieId, rating, note)
                    },
                    onDelete = { onDeleteEntry(entry.movieId, entry.title) }
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    count: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CinemaSurfaceElevated)
            .border(1.dp, CinemaBorderDark, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = count,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun WatchlistItemCard(
    entry: WatchlistEntry,
    onItemClick: () -> Unit,
    onToggleWatched: () -> Unit,
    onUpdateRating: (Float, String) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditingNote by remember { mutableStateOf(false) }
    var noteText by remember(entry.userNote) { mutableStateOf(entry.userNote) }
    val isWatched = entry.status == WatchlistStatus.WATCHED.name

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onItemClick)
            .testTag("watchlist_item_${entry.movieId}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CinemaSurfaceElevated),
        border = BorderStroke(1.dp, CinemaBorderDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Poster thumbnail
                Box(
                    modifier = Modifier
                        .width(76.dp)
                        .aspectRatio(0.72f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F0F0F))
                        .border(1.dp, CinemaBorderDark, RoundedCornerShape(10.dp))
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(entry.posterUrl)
                            .crossfade(true)
                            .error(R.drawable.img_cinema_hero)
                            .placeholder(R.drawable.img_cinema_hero)
                            .build(),
                        contentDescription = "Poster",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    CompactRatingBadge(
                        rating = entry.rating,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                    )
                }

                // Info & Actions
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = entry.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = "${entry.year} • ${entry.genres}",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Delete button
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("delete_watchlist_${entry.movieId}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Remove from watchlist",
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Status pill button (Watched vs Want to Watch)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isWatched) Color(0x3346D369) else Color(0x2238BDF8))
                            .border(
                                1.dp,
                                if (isWatched) CinemaGreen else Color(0xFF38BDF8),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable(onClick = onToggleWatched)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("status_badge_${entry.movieId}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isWatched) Icons.Default.Check else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = if (isWatched) CinemaGreen else Color(0xFF38BDF8),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = if (isWatched) "Watched" else "Want to Watch",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isWatched) CinemaGreen else Color(0xFF38BDF8)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Interactive user rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        StarRatingBar(
                            rating = entry.userRating,
                            onRatingChanged = { newRating ->
                                onUpdateRating(newRating, entry.userNote)
                            },
                            starSize = 18.dp
                        )

                        Text(
                            text = if (entry.userRating > 0) "${entry.userRating.toInt()}★" else "Rate",
                            fontSize = 11.sp,
                            color = CinemaGold,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = { isEditingNote = !isEditingNote },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Add or edit note",
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // Note preview or edit field
            if (entry.userNote.isNotBlank() && !isEditingNote) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF161616))
                        .border(0.5.dp, CinemaBorderDark, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "“${entry.userNote}”",
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFD4D4D8)
                    )
                }
            }

            AnimatedVisibility(visible = isEditingNote) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        placeholder = { Text("Add review note or favorite moments...", fontSize = 11.sp) },
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
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                onUpdateRating(entry.userRating, noteText)
                                isEditingNote = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CinemaGold),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("Save", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
