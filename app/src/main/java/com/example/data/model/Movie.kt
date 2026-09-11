package com.example.data.model

data class Movie(
    val id: String,
    val title: String,
    val year: Int,
    val rating: Double, // e.g. 8.8
    val voteCount: String, // e.g. "2.5M"
    val rottenTomatoesScore: Int, // e.g. 87 for 87%
    val metascore: Int, // e.g. 74
    val runtime: String, // e.g. "2h 28m"
    val ageRating: String, // e.g. "PG-13", "R"
    val genres: List<String>,
    val language: String = "English",
    val director: String,
    val cast: List<String>,
    val synopsis: String,
    val tagline: String,
    val posterUrl: String,
    val backdropUrl: String? = null
)
