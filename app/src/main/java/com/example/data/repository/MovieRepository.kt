package com.example.data.repository

import com.example.data.local.WatchlistDao
import com.example.data.model.Movie
import com.example.data.model.WatchlistEntry
import com.example.data.model.WatchlistStatus
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val watchlistDao: WatchlistDao) {

    val allWatchlist: Flow<List<WatchlistEntry>> = watchlistDao.getAllWatchlist()

    fun getWatchlistByStatus(status: WatchlistStatus): Flow<List<WatchlistEntry>> {
        return watchlistDao.getWatchlistByStatus(status.name)
    }

    fun isMovieInWatchlist(movieId: String): Flow<WatchlistEntry?> {
        return watchlistDao.getEntryById(movieId)
    }

    suspend fun toggleWatchlist(movie: Movie) {
        val existing = watchlistDao.getEntryByIdDirect(movie.id)
        if (existing != null) {
            watchlistDao.deleteById(movie.id)
        } else {
            val entry = WatchlistEntry(
                movieId = movie.id,
                title = movie.title,
                posterUrl = movie.posterUrl,
                year = movie.year,
                rating = movie.rating,
                genres = movie.genres.joinToString(", "),
                status = WatchlistStatus.WANT_TO_WATCH.name,
                addedAt = System.currentTimeMillis()
            )
            watchlistDao.insertOrUpdate(entry)
        }
    }

    suspend fun markAsWatched(movieId: String, isWatched: Boolean) {
        val newStatus = if (isWatched) WatchlistStatus.WATCHED.name else WatchlistStatus.WANT_TO_WATCH.name
        val watchedAt = if (isWatched) System.currentTimeMillis() else null
        watchlistDao.updateStatus(movieId, newStatus, watchedAt)
    }

    suspend fun updateReview(movieId: String, userRating: Float, userNote: String) {
        watchlistDao.updateUserReview(movieId, userRating, userNote)
    }

    suspend fun removeFromWatchlist(movieId: String) {
        watchlistDao.deleteById(movieId)
    }

    fun getAllMovies(): List<Movie> = movieCatalog

    fun searchMovies(
        query: String = "",
        selectedGenre: String = "All",
        selectedLanguage: String = "All",
        sortBy: MovieSort = MovieSort.POPULAR
    ): List<Movie> = filterMovies(query, selectedGenre, selectedLanguage, sortBy)

    fun getMovieById(id: String): Movie? {
        return movieCatalog.find { it.id == id }
    }

    companion object {
        fun filterMovies(
            query: String = "",
            selectedGenre: String = "All",
            selectedLanguage: String = "All",
            sortBy: MovieSort = MovieSort.POPULAR
        ): List<Movie> {
            var list = movieCatalog

            if (selectedGenre != "All") {
                list = list.filter { movie ->
                    movie.genres.any { it.equals(selectedGenre, ignoreCase = true) }
                }
            }

            if (selectedLanguage != "All") {
                list = list.filter { movie ->
                    movie.language.equals(selectedLanguage, ignoreCase = true)
                }
            }

            if (query.isNotBlank()) {
                val q = query.trim().lowercase()
                list = list.filter { movie ->
                    movie.title.lowercase().contains(q) ||
                    movie.director.lowercase().contains(q) ||
                    movie.language.lowercase().contains(q) ||
                    movie.genres.any { it.lowercase().contains(q) } ||
                    movie.cast.any { it.lowercase().contains(q) } ||
                    movie.synopsis.lowercase().contains(q) ||
                    movie.year.toString().contains(q)
                }
            }

            return when (sortBy) {
                MovieSort.POPULAR -> list
                MovieSort.RATING_DESC -> list.sortedByDescending { it.rating }
                MovieSort.YEAR_DESC -> list.sortedByDescending { it.year }
                MovieSort.TITLE_ASC -> list.sortedBy { it.title }
            }
        }

        val GENRES = listOf(
            "All",
            "Sci-Fi",
            "Action",
            "Drama",
            "Animation",
            "Crime",
            "Adventure",
            "Thriller",
            "Comedy",
            "Mystery",
            "Fantasy"
        )

        val LANGUAGES = listOf(
            "All",
            "English",
            "Korean",
            "Japanese",
            "French",
            "Spanish",
            "Hindi",
            "Italian",
            "German"
        )

        private val movieCatalog = listOf(
            Movie(
                id = "m1",
                title = "Oppenheimer",
                year = 2023,
                rating = 8.9,
                voteCount = "850K",
                rottenTomatoesScore = 93,
                metascore = 88,
                runtime = "3h 00m",
                ageRating = "R",
                genres = listOf("Drama", "Biography", "History"),
                director = "Christopher Nolan",
                cast = listOf("Cillian Murphy", "Emily Blunt", "Matt Damon", "Robert Downey Jr."),
                synopsis = "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb during World War II.",
                tagline = "The world forever changes.",
                posterUrl = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m2",
                title = "Interstellar",
                year = 2014,
                rating = 8.7,
                voteCount = "2.1M",
                rottenTomatoesScore = 73,
                metascore = 74,
                runtime = "2h 49m",
                ageRating = "PG-13",
                genres = listOf("Sci-Fi", "Adventure", "Drama"),
                director = "Christopher Nolan",
                cast = listOf("Matthew McConaughey", "Anne Hathaway", "Jessica Chastain", "Michael Caine"),
                synopsis = "When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot, Joseph Cooper, is tasked to pilot a spacecraft, along with a team of researchers, to find a new planet for humans.",
                tagline = "Mankind was born on Earth. It was never meant to die here.",
                posterUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m3",
                title = "Dune: Part Two",
                year = 2024,
                rating = 8.6,
                voteCount = "520K",
                rottenTomatoesScore = 92,
                metascore = 79,
                runtime = "2h 46m",
                ageRating = "PG-13",
                genres = listOf("Sci-Fi", "Action", "Adventure"),
                director = "Denis Villeneuve",
                cast = listOf("Timothée Chalamet", "Zendaya", "Rebecca Ferguson", "Javier Bardem"),
                synopsis = "Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family. Facing a choice between the love of his life and the fate of the universe, he endeavors to prevent a terrible future.",
                tagline = "Long live the fighters.",
                posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m4",
                title = "The Dark Knight",
                year = 2008,
                rating = 9.0,
                voteCount = "2.9M",
                rottenTomatoesScore = 94,
                metascore = 84,
                runtime = "2h 32m",
                ageRating = "PG-13",
                genres = listOf("Action", "Crime", "Drama"),
                director = "Christopher Nolan",
                cast = listOf("Christian Bale", "Heath Ledger", "Aaron Eckhart", "Michael Caine"),
                synopsis = "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
                tagline = "Why so serious?",
                posterUrl = "https://images.unsplash.com/photo-1509347528160-9a9e33742cdb?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m5",
                title = "Spider-Man: Across the Spider-Verse",
                year = 2023,
                rating = 8.7,
                voteCount = "410K",
                rottenTomatoesScore = 95,
                metascore = 86,
                runtime = "2h 20m",
                ageRating = "PG",
                genres = listOf("Animation", "Action", "Adventure"),
                director = "Joaquim Dos Santos, Kemp Powers",
                cast = listOf("Shameik Moore", "Hailee Steinfeld", "Brian Tyree Henry", "Oscar Isaac"),
                synopsis = "Miles Morales catapults across the Multiverse, where he encounters a team of Spider-People charged with protecting its very existence. When the heroes clash on how to handle a new threat, Miles must redefine what it means to be a hero.",
                tagline = "It's how you wear the mask that matters.",
                posterUrl = "https://images.unsplash.com/photo-1635805737707-575885ab0820?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m6",
                title = "Inception",
                year = 2010,
                rating = 8.8,
                voteCount = "2.6M",
                rottenTomatoesScore = 87,
                metascore = 74,
                runtime = "2h 28m",
                ageRating = "PG-13",
                genres = listOf("Sci-Fi", "Action", "Adventure"),
                director = "Christopher Nolan",
                cast = listOf("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page", "Tom Hardy"),
                synopsis = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster.",
                tagline = "Your mind is the scene of the crime.",
                posterUrl = "https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m7",
                title = "Parasite",
                year = 2019,
                rating = 8.5,
                voteCount = "980K",
                rottenTomatoesScore = 99,
                metascore = 96,
                runtime = "2h 12m",
                ageRating = "R",
                genres = listOf("Drama", "Thriller", "Comedy"),
                language = "Korean",
                director = "Bong Joon Ho",
                cast = listOf("Song Kang-ho", "Lee Sun-kyun", "Cho Yeo-jeong", "Choi Woo-shik"),
                synopsis = "Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.",
                tagline = "Act like you own the place.",
                posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m8",
                title = "Spirited Away",
                year = 2001,
                rating = 8.6,
                voteCount = "890K",
                rottenTomatoesScore = 96,
                metascore = 96,
                runtime = "2h 05m",
                ageRating = "PG",
                genres = listOf("Animation", "Adventure", "Fantasy"),
                language = "Japanese",
                director = "Hayao Miyazaki",
                cast = listOf("Rumi Hiiragi", "Miyu Irino", "Mari Natsuki", "Takashi Naito"),
                synopsis = "During her family's move to the suburbs, a sullen 10-year-old girl wanders into a world ruled by gods, witches and spirits, a world where humans are changed into beasts.",
                tagline = "Tunnel into another realm.",
                posterUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m9",
                title = "Blade Runner 2049",
                year = 2017,
                rating = 8.0,
                voteCount = "670K",
                rottenTomatoesScore = 88,
                metascore = 81,
                runtime = "2h 44m",
                ageRating = "R",
                genres = listOf("Sci-Fi", "Mystery", "Drama"),
                director = "Denis Villeneuve",
                cast = listOf("Ryan Gosling", "Harrison Ford", "Ana de Armas", "Sylvia Hoeks"),
                synopsis = "Young Blade Runner K's discovery of a long-buried secret leads him to track down former Blade Runner Rick Deckard, who's been missing for thirty years.",
                tagline = "The key to the future is finally unearthed.",
                posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m10",
                title = "Knives Out",
                year = 2019,
                rating = 7.9,
                voteCount = "750K",
                rottenTomatoesScore = 97,
                metascore = 82,
                runtime = "2h 10m",
                ageRating = "PG-13",
                genres = listOf("Mystery", "Comedy", "Crime"),
                director = "Rian Johnson",
                cast = listOf("Daniel Craig", "Chris Evans", "Ana de Armas", "Jamie Lee Curtis"),
                synopsis = "A detective investigates the death of a patriarch of an eccentric, combative family.",
                tagline = "Nothing connects them like murder.",
                posterUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m11",
                title = "The Shawshank Redemption",
                year = 1994,
                rating = 9.3,
                voteCount = "2.9M",
                rottenTomatoesScore = 91,
                metascore = 82,
                runtime = "2h 22m",
                ageRating = "R",
                genres = listOf("Drama", "Crime"),
                director = "Frank Darabont",
                cast = listOf("Tim Robbins", "Morgan Freeman", "Bob Gunton", "William Sadler"),
                synopsis = "Over the course of several years, two convicts form a friendship, seeking consolation and, eventually, redemption through basic compassion.",
                tagline = "Fear can hold you prisoner. Hope can set you free.",
                posterUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m12",
                title = "La La Land",
                year = 2016,
                rating = 8.0,
                voteCount = "680K",
                rottenTomatoesScore = 91,
                metascore = 94,
                runtime = "2h 08m",
                ageRating = "PG-13",
                genres = listOf("Comedy", "Drama", "Music"),
                director = "Damien Chazelle",
                cast = listOf("Ryan Gosling", "Emma Stone", "John Legend", "Rosemarie DeWitt"),
                synopsis = "While navigating their careers in Los Angeles, a pianist and an actress fall in love while attempting to reconcile their aspirations for the future.",
                tagline = "Here's to the fools who dream.",
                posterUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m13",
                title = "Everything Everywhere All at Once",
                year = 2022,
                rating = 7.8,
                voteCount = "560K",
                rottenTomatoesScore = 94,
                metascore = 81,
                runtime = "2h 19m",
                ageRating = "R",
                genres = listOf("Sci-Fi", "Adventure", "Comedy"),
                director = "Daniel Kwan, Daniel Scheinert",
                cast = listOf("Michelle Yeoh", "Stephanie Hsu", "Ke Huy Quan", "Jamie Lee Curtis"),
                synopsis = "A middle-aged Chinese immigrant is swept up into an insane adventure in which she alone can save existence by exploring other universes and connecting with the lives she could have led.",
                tagline = "The universe is so much bigger than your problems.",
                posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m14",
                title = "Mad Max: Fury Road",
                year = 2015,
                rating = 8.1,
                voteCount = "1.1M",
                rottenTomatoesScore = 97,
                metascore = 90,
                runtime = "2h 00m",
                ageRating = "R",
                genres = listOf("Action", "Adventure", "Sci-Fi"),
                director = "George Miller",
                cast = listOf("Tom Hardy", "Charlize Theron", "Nicholas Hoult", "Hugh Keays-Byrne"),
                synopsis = "In a post-apocalyptic wasteland, a woman rebels against a tyrannical ruler in search for her homeland with the aid of a group of female prisoners, a psychotic worshiper and a drifter named Max.",
                tagline = "What a lovely day.",
                posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m15",
                title = "Pulp Fiction",
                year = 1994,
                rating = 8.9,
                voteCount = "2.2M",
                rottenTomatoesScore = 92,
                metascore = 95,
                runtime = "2h 34m",
                ageRating = "R",
                genres = listOf("Crime", "Drama"),
                director = "Quentin Tarantino",
                cast = listOf("John Travolta", "Uma Thurman", "Samuel L. Jackson", "Bruce Willis"),
                synopsis = "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.",
                tagline = "Just because you are a character doesn't mean you have character.",
                posterUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m16",
                title = "The Grand Budapest Hotel",
                year = 2014,
                rating = 8.1,
                voteCount = "890K",
                rottenTomatoesScore = 92,
                metascore = 88,
                runtime = "1h 39m",
                ageRating = "R",
                genres = listOf("Comedy", "Adventure", "Crime"),
                director = "Wes Anderson",
                cast = listOf("Ralph Fiennes", "F. Murray Abraham", "Mathieu Amalric", "Adrien Brody"),
                synopsis = "A writer encounters the owner of an aging high-class hotel, who tells him of his early years serving as a lobby boy in the hotel's glorious years under an exceptional concierge.",
                tagline = "A captivating tale of friendship, theft and murder.",
                posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m17",
                title = "Amélie",
                year = 2001,
                rating = 8.3,
                voteCount = "780K",
                rottenTomatoesScore = 89,
                metascore = 69,
                runtime = "2h 02m",
                ageRating = "R",
                genres = listOf("Comedy", "Romance"),
                language = "French",
                director = "Jean-Pierre Jeunet",
                cast = listOf("Audrey Tautou", "Mathieu Kassovitz", "Rufus", "Lorella Cravotta"),
                synopsis = "Amélie is an innocent and naive girl in Paris with her own sense of justice. She decides to help those around her and, along the way, discovers love.",
                tagline = "She'll change your life.",
                posterUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m18",
                title = "Pan's Labyrinth",
                year = 2006,
                rating = 8.2,
                voteCount = "690K",
                rottenTomatoesScore = 95,
                metascore = 98,
                runtime = "1h 58m",
                ageRating = "R",
                genres = listOf("Drama", "Fantasy", "War"),
                language = "Spanish",
                director = "Guillermo del Toro",
                cast = listOf("Ivana Baquero", "Sergi López", "Maribel Verdú", "Doug Jones"),
                synopsis = "In the Falangist Spain of 1944, the bookish young stepdaughter of a sadistic army officer escapes into an eerie but captivating fantasy world.",
                tagline = "Innocence has a power evil cannot imagine.",
                posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m19",
                title = "RRR",
                year = 2022,
                rating = 7.8,
                voteCount = "170K",
                rottenTomatoesScore = 95,
                metascore = 83,
                runtime = "3h 07m",
                ageRating = "PG-13",
                genres = listOf("Action", "Drama"),
                language = "Hindi",
                director = "S.S. Rajamouli",
                cast = listOf("N.T. Rama Rao Jr.", "Ram Charan", "Ajay Devgn", "Alia Bhatt"),
                synopsis = "A fearless revolutionary and an officer in the British force, who once were friends, unite against a common enemy in 1920s colonial India.",
                tagline = "Rise, Roar, Revolt.",
                posterUrl = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m20",
                title = "Life Is Beautiful",
                year = 1997,
                rating = 8.6,
                voteCount = "740K",
                rottenTomatoesScore = 80,
                metascore = 59,
                runtime = "1h 56m",
                ageRating = "PG-13",
                genres = listOf("Comedy", "Drama", "Romance"),
                language = "Italian",
                director = "Roberto Benigni",
                cast = listOf("Roberto Benigni", "Nicoletta Braschi", "Giorgio Cantarini", "Giustino Durano"),
                synopsis = "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp.",
                tagline = "An unforgettable fable that proves love, family and imagination conquer all.",
                posterUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m21",
                title = "Godzilla Minus One",
                year = 2023,
                rating = 8.3,
                voteCount = "140K",
                rottenTomatoesScore = 98,
                metascore = 81,
                runtime = "2h 04m",
                ageRating = "PG-13",
                genres = listOf("Action", "Sci-Fi", "Drama"),
                language = "Japanese",
                director = "Takashi Yamazaki",
                cast = listOf("Ryunosuke Kamiki", "Minami Hamabe", "Yuki Yamada", "Munetaka Aoki"),
                synopsis = "Post-war Japan is at its lowest point when a new crisis arises in the form of a giant monster, baptized in the horrific power of the atomic bomb.",
                tagline = "Survive and fight back.",
                posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m22",
                title = "Past Lives",
                year = 2023,
                rating = 7.9,
                voteCount = "150K",
                rottenTomatoesScore = 95,
                metascore = 94,
                runtime = "1h 45m",
                ageRating = "PG-13",
                genres = listOf("Drama", "Romance"),
                language = "Korean",
                director = "Celine Song",
                cast = listOf("Greta Lee", "Teo Yoo", "John Magaro", "Moon Seung-ah"),
                synopsis = "Nora and Hae Sung, two deeply connected childhood friends, are wrested apart after Nora's family emigrates from South Korea. Two decades later, they are reunited in New York for one fateful week.",
                tagline = "In-Yun. It means providence or fate.",
                posterUrl = "https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m23",
                title = "Anatomy of a Fall",
                year = 2023,
                rating = 7.7,
                voteCount = "130K",
                rottenTomatoesScore = 96,
                metascore = 86,
                runtime = "2h 31m",
                ageRating = "R",
                genres = listOf("Crime", "Drama", "Mystery"),
                language = "French",
                director = "Justine Triet",
                cast = listOf("Sandra Hüller", "Swann Arlaud", "Milo Machado Graner", "Antoine Reinartz"),
                synopsis = "A woman is suspected of murder after her husband's death in the snow in an isolated chalet. The trial turns into an intimate journey through their precarious relationship.",
                tagline = "Did he fall or was he pushed?",
                posterUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m24",
                title = "The Lives of Others",
                year = 2006,
                rating = 8.4,
                voteCount = "410K",
                rottenTomatoesScore = 92,
                metascore = 89,
                runtime = "2h 17m",
                ageRating = "R",
                genres = listOf("Drama", "Mystery", "Thriller"),
                language = "German",
                director = "Florian Henckel von Donnersmarck",
                cast = listOf("Ulrich Mühe", "Martina Gedeck", "Sebastian Koch", "Ulrich Tukur"),
                synopsis = "In 1984 East Berlin, an agent of the secret police, conducting surveillance on a writer and his lover, finds himself becoming increasingly absorbed by their lives.",
                tagline = "Before the Fall of the Berlin Wall, one man chose compassion.",
                posterUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m25",
                title = "Roma",
                year = 2018,
                rating = 7.7,
                voteCount = "170K",
                rottenTomatoesScore = 96,
                metascore = 96,
                runtime = "2h 15m",
                ageRating = "R",
                genres = listOf("Drama"),
                language = "Spanish",
                director = "Alfonso Cuarón",
                cast = listOf("Yalitza Aparicio", "Marina de Tavira", "Diego Cortina Autrey"),
                synopsis = "A year in the life of a middle-class family's maid in Mexico City in the early 1970s.",
                tagline = "There are periods in history that scar a society and moments in life that transform us as individuals.",
                posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m26",
                title = "Dangal",
                year = 2016,
                rating = 8.3,
                voteCount = "210K",
                rottenTomatoesScore = 88,
                metascore = 82,
                runtime = "2h 41m",
                ageRating = "PG",
                genres = listOf("Action", "Biography", "Drama"),
                language = "Hindi",
                director = "Nitesh Tiwari",
                cast = listOf("Aamir Khan", "Fatima Sana Shaikh", "Sanya Malhotra", "Sakshi Tanwar"),
                synopsis = "Former wrestler Mahavir Singh Phogat and his two wrestler daughters struggle towards glory at the Commonwealth Games in the face of societal oppression.",
                tagline = "Mhaari chhoriyan chhoron se kam hain ke?",
                posterUrl = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m27",
                title = "Cinema Paradiso",
                year = 1988,
                rating = 8.5,
                voteCount = "290K",
                rottenTomatoesScore = 90,
                metascore = 80,
                runtime = "2h 35m",
                ageRating = "PG",
                genres = listOf("Drama", "Romance"),
                language = "Italian",
                director = "Giuseppe Tornatore",
                cast = listOf("Philippe Noiret", "Salvatore Cascio", "Marco Leonardi", "Jacques Perrin"),
                synopsis = "A filmmaker recalls his childhood when falling in love with the pictures at the cinema of his home village and forms a deep friendship with the cinema's projectionist.",
                tagline = "A celebration of youth, friendship, and the everlasting magic of the movies.",
                posterUrl = "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=600&auto=format&fit=crop&q=80"
            ),
            Movie(
                id = "m28",
                title = "Run Lola Run",
                year = 1998,
                rating = 7.6,
                voteCount = "210K",
                rottenTomatoesScore = 93,
                metascore = 77,
                runtime = "1h 21m",
                ageRating = "R",
                genres = listOf("Crime", "Drama", "Thriller"),
                language = "German",
                director = "Tom Tykwer",
                cast = listOf("Franka Potente", "Moritz Bleibtreu", "Herbert Knaup", "Nina Petri"),
                synopsis = "After a botched money delivery, Lola has 20 minutes to come up with 100,000 Deutschmarks to save her boyfriend's life.",
                tagline = "Every second of every day you're faced with a decision that can change your life.",
                posterUrl = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=600&auto=format&fit=crop&q=80"
            )
        )

        fun getLanguageCategories(): List<LanguageCategory> {
            val counts = movieCatalog.groupBy { it.language }.mapValues { it.value.size }
            val totalCount = movieCatalog.size
            return listOf(
                LanguageCategory("All", "All Languages", "World", "🌐", totalCount),
                LanguageCategory("English", "English", "English", "🇺🇸", counts["English"] ?: 0),
                LanguageCategory("Korean", "Korean", "한국어", "🇰🇷", counts["Korean"] ?: 0),
                LanguageCategory("Japanese", "Japanese", "日本語", "🇯🇵", counts["Japanese"] ?: 0),
                LanguageCategory("French", "French", "Français", "🇫🇷", counts["French"] ?: 0),
                LanguageCategory("Spanish", "Spanish", "Español", "🇪🇸", counts["Spanish"] ?: 0),
                LanguageCategory("Hindi", "Hindi", "हिन्दी", "🇮🇳", counts["Hindi"] ?: 0),
                LanguageCategory("Italian", "Italian", "Italiano", "🇮🇹", counts["Italian"] ?: 0),
                LanguageCategory("German", "German", "Deutsch", "🇩🇪", counts["German"] ?: 0)
            )
        }
    }

    fun getLanguageCategories(): List<LanguageCategory> = Companion.getLanguageCategories()
}

data class LanguageCategory(
    val id: String,
    val name: String,
    val nativeName: String,
    val flag: String,
    val movieCount: Int
)

enum class MovieSort(val label: String) {
    POPULAR("Popular"),
    RATING_DESC("Highest Rated"),
    YEAR_DESC("Newest"),
    TITLE_ASC("Title (A-Z)")
}
