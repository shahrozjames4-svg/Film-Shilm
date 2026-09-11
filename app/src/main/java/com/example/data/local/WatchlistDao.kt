package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.WatchlistEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    fun getAllWatchlist(): Flow<List<WatchlistEntry>>

    @Query("SELECT * FROM watchlist WHERE status = :status ORDER BY addedAt DESC")
    fun getWatchlistByStatus(status: String): Flow<List<WatchlistEntry>>

    @Query("SELECT * FROM watchlist WHERE movieId = :movieId LIMIT 1")
    fun getEntryById(movieId: String): Flow<WatchlistEntry?>

    @Query("SELECT * FROM watchlist WHERE movieId = :movieId LIMIT 1")
    suspend fun getEntryByIdDirect(movieId: String): WatchlistEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entry: WatchlistEntry)

    @Update
    suspend fun update(entry: WatchlistEntry)

    @Query("DELETE FROM watchlist WHERE movieId = :movieId")
    suspend fun deleteById(movieId: String)

    @Query("UPDATE watchlist SET status = :status, watchedAt = :watchedAt WHERE movieId = :movieId")
    suspend fun updateStatus(movieId: String, status: String, watchedAt: Long?)

    @Query("UPDATE watchlist SET userRating = :userRating, userNote = :userNote WHERE movieId = :movieId")
    suspend fun updateUserReview(movieId: String, userRating: Float, userNote: String)
}
