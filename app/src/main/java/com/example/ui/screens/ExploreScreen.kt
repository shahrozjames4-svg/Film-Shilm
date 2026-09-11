package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Movie
import com.example.data.repository.LanguageCategory
import com.example.data.repository.MovieRepository
import com.example.data.repository.MovieSort
import com.example.ui.components.MovieCard
import com.example.ui.theme.CinemaBorderDark
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaObsidian
import com.example.ui.theme.CinemaSurfaceDark
import com.example.ui.theme.CinemaSurfaceElevated

@Composable
fun ExploreScreen(
    movies: List<Movie>,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    selectedGenre: String,
    onGenreSelected: (String) -> Unit,
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    languageCategories: List<LanguageCategory> = MovieRepository.getLanguageCategories(),
    selectedSort: MovieSort,
    onSortSelected: (MovieSort) -> Unit,
    isMovieInWatchlist: (String) -> Boolean,
    onMovieClick: (Movie) -> Unit,
    onWatchlistToggle: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSortMenu by remember { mutableStateOf(false) }

    fun getLanguageEmoji(language: String): String {
        return when (language.lowercase()) {
            "all" -> "🌐"
            "english" -> "🇺🇸"
            "korean" -> "🇰🇷"
            "japanese" -> "🇯🇵"
            "french" -> "🇫🇷"
            "spanish" -> "🇪🇸"
            "hindi" -> "🇮🇳"
            "italian" -> "🇮🇹"
            "german" -> "🇩🇪"
            else -> "🎬"
        }
    }

    val hasActiveFilter = selectedLanguage != "All" || selectedGenre != "All" || searchQuery.isNotBlank()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = modifier
            .fillMaxSize()
            .background(CinemaObsidian)
            .testTag("explore_movie_grid"),
        contentPadding = PaddingValues(bottom = 96.dp, start = 14.dp, end = 14.dp, top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Header & Search Bar
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            ) {
                // Atmospheric Cinema Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, CinemaBorderDark, RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_cinema_hero),
                        contentDescription = "Cinema atmosphere",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xF5080808),
                                        Color(0xBB080808),
                                        Color(0x44000000)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CinemaGold)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "CINEMA VAULT",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black
                                )
                            }
                            Text(
                                text = "EXPLORE FILMS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Find Great Movies & Ratings",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Text(
                            text = "Browse critics scores, audience reviews & watchlist your favorites.",
                            fontSize = 12.sp,
                            color = Color(0xFFD4D4D8),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Input Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    placeholder = {
                        Text(
                            "Search by title, director, actor, genre...",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = CinemaGold
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { onSearchQueryChanged("") },
                                modifier = Modifier.testTag("clear_search_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search",
                                    tint = Color.LightGray
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("movie_search_input"),
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

                Spacer(modifier = Modifier.height(14.dp))

                // Language Category Section Header & Cards Carousel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "🌐",
                            fontSize = 14.sp
                        )
                        Text(
                            text = "BROWSE BY LANGUAGE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CinemaGold,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF242424))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${languageCategories.filter { it.id != "All" }.size} Languages",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.LightGray
                            )
                        }
                    }

                    if (selectedLanguage != "All") {
                        Text(
                            text = "Reset Language",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CinemaGold,
                            modifier = Modifier
                                .clickable { onLanguageSelected("All") }
                                .padding(4.dp)
                                .testTag("reset_language_btn")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Language Category Cards Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    languageCategories.forEach { cat ->
                        val isSelected = selectedLanguage.equals(cat.id, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .width(118.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) Color(0xFF2D2312) else CinemaSurfaceElevated
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) CinemaGold else CinemaBorderDark,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { onLanguageSelected(cat.id) }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                .testTag("language_card_${cat.id}")
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = cat.flag,
                                        fontSize = 20.sp
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(
                                                if (isSelected) CinemaGold else Color(0xFF1E1E1E)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "${cat.movieCount}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.Black else Color.Gray
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = cat.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) CinemaGold else Color.White,
                                    maxLines = 1
                                )

                                Text(
                                    text = cat.nativeName,
                                    fontSize = 10.sp,
                                    color = if (isSelected) Color(0xFFE5C158) else Color.Gray,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                // If specific language is active, show spotlight banner
                if (selectedLanguage != "All") {
                    val activeCat = languageCategories.find { it.id.equals(selectedLanguage, ignoreCase = true) }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1F1A10))
                            .border(1.dp, CinemaGold.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = activeCat?.flag ?: "🎬",
                                    fontSize = 18.sp
                                )
                                Column {
                                    Text(
                                        text = "Showing ${activeCat?.name ?: selectedLanguage} Cinema",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${movies.size} title(s) in ${activeCat?.nativeName ?: selectedLanguage}",
                                        fontSize = 10.sp,
                                        color = CinemaGold
                                    )
                                }
                            }

                            IconButton(
                                onClick = { onLanguageSelected("All") },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear language filter",
                                    tint = CinemaGold,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Genre Filter Chips & Sort Button Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "GENRE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MovieRepository.GENRES.forEach { genre ->
                            val isSelected = selectedGenre.equals(genre, ignoreCase = true)
                            FilterChip(
                                selected = isSelected,
                                onClick = { onGenreSelected(genre) },
                                label = {
                                    Text(
                                        text = genre,
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
                                modifier = Modifier.testTag("genre_chip_$genre")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Sort menu button
                    Box {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(CinemaSurfaceElevated)
                                .border(1.dp, CinemaBorderDark, RoundedCornerShape(10.dp))
                                .clickable { showSortMenu = true }
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .testTag("sort_menu_btn")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sort,
                                    contentDescription = "Sort",
                                    tint = CinemaGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = selectedSort.label,
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false },
                            modifier = Modifier
                                .background(CinemaSurfaceElevated)
                                .border(1.dp, CinemaBorderDark, RoundedCornerShape(8.dp))
                        ) {
                            MovieSort.values().forEach { sort ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = sort.label,
                                            color = if (selectedSort == sort) CinemaGold else Color.White,
                                            fontWeight = if (selectedSort == sort) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        onSortSelected(sort)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Active Filters Row (if any filter is applied)
                if (hasActiveFilter) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Active:",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )

                        if (selectedLanguage != "All") {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF222222))
                                    .border(1.dp, CinemaGold, RoundedCornerShape(12.dp))
                                    .clickable { onLanguageSelected("All") }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "${getLanguageEmoji(selectedLanguage)} $selectedLanguage",
                                        fontSize = 11.sp,
                                        color = CinemaGold,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Remove language filter",
                                        tint = CinemaGold,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }

                        if (selectedGenre != "All") {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF222222))
                                    .border(1.dp, CinemaGold, RoundedCornerShape(12.dp))
                                    .clickable { onGenreSelected("All") }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = selectedGenre,
                                        fontSize = 11.sp,
                                        color = CinemaGold,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Remove genre filter",
                                        tint = CinemaGold,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }

                        if (searchQuery.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF222222))
                                    .border(1.dp, CinemaBorderDark, RoundedCornerShape(12.dp))
                                    .clickable { onSearchQueryChanged("") }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "\"$searchQuery\"",
                                        fontSize = 11.sp,
                                        color = Color.White
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear search",
                                        tint = Color.LightGray,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }

                        // Clear all button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onLanguageSelected("All")
                                    onGenreSelected("All")
                                    onSearchQueryChanged("")
                                }
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Reset all",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Results count
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.isNotBlank()) "RESULTS FOR \"$searchQuery\"" else "FEATURED FILMS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${movies.size} titles",
                        fontSize = 11.sp,
                        color = CinemaGold,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Empty state when search yields no matches
        if (movies.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp)
                        .testTag("empty_search_results"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(CinemaSurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalMovies,
                            contentDescription = null,
                            tint = CinemaGold,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "No movies found",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (hasActiveFilter) "No movies match your current language, genre, or search filters." else "Try searching with a different movie title, actor, or genre.",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )

                    if (hasActiveFilter) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                onLanguageSelected("All")
                                onGenreSelected("All")
                                onSearchQueryChanged("")
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CinemaGold,
                                contentColor = Color.Black
                            ),
                            modifier = Modifier.testTag("reset_filters_btn")
                        ) {
                            Text(
                                text = "Reset All Filters",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        } else {
            items(movies, key = { it.id }) { movie ->
                MovieCard(
                    movie = movie,
                    isInWatchlist = isMovieInWatchlist(movie.id),
                    onMovieClick = { onMovieClick(movie) },
                    onWatchlistToggle = { onWatchlistToggle(movie) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
