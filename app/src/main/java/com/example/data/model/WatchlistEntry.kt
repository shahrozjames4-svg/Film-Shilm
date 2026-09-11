package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class WatchlistStatus {
    WANT_TO_WATCH,
    WATCHED
}

@Entity(tableName = "watchlist")
data class WatchlistEntry(
    @PrimaryKey
    val movieId: String,
    val title: String,
    val posterUrl: String,
    val year: Int,
    val rating: Double,
    val genres: String, // Comma separated
    val status: String = WatchlistStatus.WANT_TO_WATCH.name,
    val userRating: Float = 0f, // 0 to 5 stars
    val userNote: String = "",
    val addedAt: Long = System.currentTimeMillis(),
    val watchedAt: Long? = null
)
