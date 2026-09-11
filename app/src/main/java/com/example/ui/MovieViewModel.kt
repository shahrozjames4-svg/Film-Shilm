package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.Movie
import com.example.data.model.WatchlistEntry
import com.example.data.model.WatchlistStatus
import com.example.data.repository.LanguageCategory
import com.example.data.repository.MovieRepository
import com.example.data.repository.MovieSort
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class WatchlistFilter(val label: String) {
    ALL("All"),
    WANT_TO_WATCH("Want to Watch"),
    WATCHED("Watched")
}

data class SearchUiState(
    val query: String = "",
    val selectedGenre: String = "All",
    val selectedSort: MovieSort = MovieSort.POPULAR,
    val results: List<Movie> = emptyList()
)

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = MovieRepository(database.watchlistDao())
    }

    // Search & Browse state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedGenre = MutableStateFlow("All")
    val selectedGenre = _selectedGenre.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("All")
    val selectedLanguage = _selectedLanguage.asStateFlow()

    val languageCategories: List<LanguageCategory> = repository.getLanguageCategories()

    private val _selectedSort = MutableStateFlow(MovieSort.POPULAR)
    val selectedSort = _selectedSort.asStateFlow()

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies = _movies.asStateFlow()

    // Watchlist reactive state from Room
    val allWatchlist: StateFlow<List<WatchlistEntry>> = repository.allWatchlist
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _watchlistFilter = MutableStateFlow(WatchlistFilter.ALL)
    val watchlistFilter = _watchlistFilter.asStateFlow()

    private val _watchlistSearchQuery = MutableStateFlow("")
    val watchlistSearchQuery = _watchlistSearchQuery.asStateFlow()

    val filteredWatchlist: StateFlow<List<WatchlistEntry>> = combine(
        allWatchlist,
        _watchlistFilter,
        _watchlistSearchQuery
    ) { list, filter, query ->
        var res = list
        if (filter == WatchlistFilter.WANT_TO_WATCH) {
            res = res.filter { it.status == WatchlistStatus.WANT_TO_WATCH.name }
        } else if (filter == WatchlistFilter.WATCHED) {
            res = res.filter { it.status == WatchlistStatus.WATCHED.name }
        }

        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            res = res.filter {
                it.title.lowercase().contains(q) ||
                it.genres.lowercase().contains(q) ||
                it.year.toString().contains(q)
            }
        }
        res
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Currently selected movie for full details
    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie = _selectedMovie.asStateFlow()

    // Snackbar event
    private val _messageEvent = MutableSharedFlow<String>()
    val messageEvent: SharedFlow<String> = _messageEvent.asSharedFlow()

    init {
        refreshMovies()
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        refreshMovies()
    }

    fun onGenreSelected(genre: String) {
        _selectedGenre.value = genre
        refreshMovies()
    }

    fun onLanguageSelected(language: String) {
        _selectedLanguage.value = language
        refreshMovies()
    }

    fun onSortSelected(sort: MovieSort) {
        _selectedSort.value = sort
        refreshMovies()
    }

    private fun refreshMovies() {
        _movies.value = repository.searchMovies(
            query = _searchQuery.value,
            selectedGenre = _selectedGenre.value,
            selectedLanguage = _selectedLanguage.value,
            sortBy = _selectedSort.value
        )
    }

    fun onWatchlistFilterChanged(filter: WatchlistFilter) {
        _watchlistFilter.value = filter
    }

    fun onWatchlistSearchQueryChanged(query: String) {
        _watchlistSearchQuery.value = query
    }

    fun selectMovie(movie: Movie?) {
        _selectedMovie.value = movie
    }

    fun toggleWatchlist(movie: Movie) {
        viewModelScope.launch {
            val isSaved = allWatchlist.value.any { it.movieId == movie.id }
            repository.toggleWatchlist(movie)
            if (isSaved) {
                _messageEvent.emit("Removed \"${movie.title}\" from Watchlist")
            } else {
                _messageEvent.emit("Saved \"${movie.title}\" to Watchlist")
            }
        }
    }

    fun toggleWatchedStatus(entry: WatchlistEntry) {
        viewModelScope.launch {
            val isNowWatched = entry.status != WatchlistStatus.WATCHED.name
            repository.markAsWatched(entry.movieId, isNowWatched)
            val msg = if (isNowWatched) "Marked as Watched" else "Moved back to Want to Watch"
            _messageEvent.emit(msg)
        }
    }

    fun updateReview(movieId: String, rating: Float, note: String) {
        viewModelScope.launch {
            repository.updateReview(movieId, rating, note)
            _messageEvent.emit("Saved your rating and note")
        }
    }

    fun removeFromWatchlist(movieId: String, title: String) {
        viewModelScope.launch {
            repository.removeFromWatchlist(movieId)
            _messageEvent.emit("Removed \"$title\" from Watchlist")
        }
    }

    fun isMovieInWatchlist(movieId: String): Boolean {
        return allWatchlist.value.any { it.movieId == movieId }
    }

    fun getAllMovies(): List<Movie> = repository.getAllMovies()

    fun getWatchlistEntry(movieId: String): WatchlistEntry? {
        return allWatchlist.value.find { it.movieId == movieId }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MovieViewModel(application) as T
        }
    }
}
