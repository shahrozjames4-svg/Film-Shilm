package com.example

import com.example.data.repository.MovieRepository
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testLanguageFiltering() {
    val koreanMovies = MovieRepository.filterMovies(selectedLanguage = "Korean")
    assertTrue(koreanMovies.isNotEmpty())
    assertTrue(koreanMovies.all { it.language.equals("Korean", ignoreCase = true) })

    val japaneseMovies = MovieRepository.filterMovies(selectedLanguage = "Japanese")
    assertTrue(japaneseMovies.isNotEmpty())
    assertTrue(japaneseMovies.all { it.language.equals("Japanese", ignoreCase = true) })

    val allMovies = MovieRepository.filterMovies(selectedLanguage = "All")
    assertTrue(allMovies.size >= 20)
  }

  @Test
  fun testLanguageCategories() {
    val categories = MovieRepository.getLanguageCategories()
    assertTrue(categories.isNotEmpty())

    val allCat = categories.find { it.id == "All" }
    assertNotNull(allCat)
    assertTrue(allCat!!.movieCount >= 20)

    val koreanCat = categories.find { it.id == "Korean" }
    assertNotNull(koreanCat)
    assertEquals(2, koreanCat!!.movieCount)
    assertEquals("한국어", koreanCat.nativeName)
    assertEquals("🇰🇷", koreanCat.flag)

    val japaneseCat = categories.find { it.id == "Japanese" }
    assertNotNull(japaneseCat)
    assertEquals(2, japaneseCat!!.movieCount)
    assertEquals("日本語", japaneseCat.nativeName)
    assertEquals("🇯🇵", japaneseCat.flag)

    val spanishCat = categories.find { it.id == "Spanish" }
    assertNotNull(spanishCat)
    assertEquals(2, spanishCat!!.movieCount)
    assertEquals("Español", spanishCat.nativeName)
    assertEquals("🇪🇸", spanishCat.flag)
  }
}
