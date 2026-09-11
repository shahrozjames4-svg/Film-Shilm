package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.repository.MovieSort
import com.example.ui.components.MovieDetailDialog
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.CinemaBorderDark
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaObsidian
import com.example.ui.theme.CinemaSurfaceDark
import com.example.ui.theme.CinemaSurfaceElevated

enum class AppTab(val label: String) {
    EXPLORE("Explore"),
    WATCHLIST("Watchlist")
}

@Composable
fun MoviesApp(
    viewModel: MovieViewModel,
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf(AppTab.EXPLORE) }
    val snackbarHostState = remember { SnackbarHostState() }

    val movies by viewModel.movies.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedGenre by viewModel.selectedGenre.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val selectedSort by viewModel.selectedSort.collectAsStateWithLifecycle()

    val allWatchlist by viewModel.allWatchlist.collectAsStateWithLifecycle()
    val filteredWatchlist by viewModel.filteredWatchlist.collectAsStateWithLifecycle()
    val watchlistFilter by viewModel.watchlistFilter.collectAsStateWithLifecycle()
    val watchlistSearchQuery by viewModel.watchlistSearchQuery.collectAsStateWithLifecycle()

    val selectedMovie by viewModel.selectedMovie.collectAsStateWithLifecycle()

    // Listen to snackbar messages
    LaunchedEffect(viewModel) {
        viewModel.messageEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CinemaObsidian,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .testTag("app_snackbar")
            )
        },
        topBar = {
            // Sleek Top App Bar in Jet Black with subtle smoky border
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CinemaSurfaceDark)
                    .drawBehind {
                        drawLine(
                            color = CinemaBorderDark,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CinemaGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Movie,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "MOVIES",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF222222))
                            .border(0.5.dp, CinemaBorderDark, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "PRO",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = CinemaGold
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .drawBehind {
                        drawLine(
                            color = CinemaBorderDark,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    .testTag("app_bottom_navigation"),
                containerColor = CinemaSurfaceDark,
                tonalElevation = 0.dp
            ) {
                // Explore Tab
                NavigationBarItem(
                    selected = currentTab == AppTab.EXPLORE,
                    onClick = { currentTab = AppTab.EXPLORE },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.EXPLORE) Icons.Filled.Movie else Icons.Outlined.Movie,
                            contentDescription = "Explore",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Explore",
                            fontSize = 11.sp,
                            fontWeight = if (currentTab == AppTab.EXPLORE) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = CinemaGold,
                        indicatorColor = CinemaGold,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("nav_explore")
                )

                // Watchlist Tab
                val savedCount = allWatchlist.size
                NavigationBarItem(
                    selected = currentTab == AppTab.WATCHLIST,
                    onClick = { currentTab = AppTab.WATCHLIST },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (savedCount > 0) {
                                    Badge(
                                        containerColor = CinemaGold,
                                        contentColor = Color.Black
                                    ) {
                                        Text("$savedCount", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (currentTab == AppTab.WATCHLIST) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Watchlist",
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = "Watchlist",
                            fontSize = 11.sp,
                            fontWeight = if (currentTab == AppTab.WATCHLIST) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = CinemaGold,
                        indicatorColor = CinemaGold,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("nav_watchlist")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.EXPLORE -> {
                    ExploreScreen(
                        movies = movies,
                        searchQuery = searchQuery,
                        onSearchQueryChanged = viewModel::onSearchQueryChanged,
                        selectedGenre = selectedGenre,
                        onGenreSelected = viewModel::onGenreSelected,
                        selectedLanguage = selectedLanguage,
                        onLanguageSelected = viewModel::onLanguageSelected,
                        languageCategories = viewModel.languageCategories,
                        selectedSort = selectedSort,
                        onSortSelected = viewModel::onSortSelected,
                        isMovieInWatchlist = viewModel::isMovieInWatchlist,
                        onMovieClick = viewModel::selectMovie,
                        onWatchlistToggle = viewModel::toggleWatchlist
                    )
                }

                AppTab.WATCHLIST -> {
                    WatchlistScreen(
                        allWatchlist = allWatchlist,
                        filteredWatchlist = filteredWatchlist,
                        currentFilter = watchlistFilter,
                        onFilterChanged = viewModel::onWatchlistFilterChanged,
                        searchQuery = watchlistSearchQuery,
                        onSearchQueryChanged = viewModel::onWatchlistSearchQueryChanged,
                        onEntryClick = { movieId ->
                            val m = viewModel.getAllMovies().find { it.id == movieId }
                            viewModel.selectMovie(m)
                        },
                        onToggleWatched = viewModel::toggleWatchedStatus,
                        onUpdateRating = viewModel::updateReview,
                        onDeleteEntry = viewModel::removeFromWatchlist,
                        onExploreClicked = { currentTab = AppTab.EXPLORE }
                    )
                }
            }
        }

        // Movie Detail Dialog
        selectedMovie?.let { movie ->
            val entry = viewModel.getWatchlistEntry(movie.id)
            MovieDetailDialog(
                movie = movie,
                watchlistEntry = entry,
                onDismiss = { viewModel.selectMovie(null) },
                onToggleWatchlist = { viewModel.toggleWatchlist(movie) },
                onToggleWatched = {
                    if (entry != null) {
                        viewModel.toggleWatchedStatus(entry)
                    }
                },
                onSaveReview = { rating, note ->
                    viewModel.updateReview(movie.id, rating, note)
                }
            )
        }
    }
}
