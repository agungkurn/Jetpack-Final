package id.ak.movieshightlight.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import id.ak.movieshightlight.data.api.DiscoverMovieItem
import id.ak.movieshightlight.data.api.MovieDetailResponse
import id.ak.movieshightlight.domain.ResultStatus
import id.ak.movieshightlight.domain.ui.DetailsItem
import id.ak.movieshightlight.domain.ui.DiscoverItem
import id.ak.movieshightlight.getOrAwaitValue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness

@RunWith(MockitoJUnitRunner.Silent::class)
class MovieRepositoryTest {
	@get:Rule
	val lenientRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.LENIENT)

	@get:Rule
	val rule = InstantTaskExecutorRule()

	private lateinit var repository: MovieRepository

	@Mock
	private lateinit var remoteDataSource: MovieRemoteDataSource

	@Mock
	private lateinit var localDataSource: FilmLocalDataSource

	@Mock
	private lateinit var favorites: PagedList<DiscoverItem>

	private val dummyList = listOf(DiscoverMovieItem())

	private val expectedMovieId = 1

	private val expectedIsFavorite = false

	@Before
	fun setUp() {
		repository = MovieRepository.getInstance(remoteDataSource, localDataSource)

		`when`(remoteDataSource.getAllMovies()).thenReturn(flow { emit(dummyList) })
		`when`(favorites.size).thenReturn(5)
		`when`(localDataSource.isFavorite(expectedMovieId)).thenReturn(
			flow {
				emit(expectedIsFavorite)
			}
		)
		`when`(localDataSource.getFavoriteMovies()).thenReturn(MutableLiveData(favorites))
	}

	@Test
	fun getMovies() {
		runBlocking {
			repository.getMovies().collect {
				if (it is ResultStatus.Success) {
					assert(it.data.isNotEmpty())
				}
			}
		}
	}

	@Test
	fun getMovieDetailsFromApi() {
		`when`(localDataSource.getDetails(expectedMovieId)).thenReturn(
			flow { emit(emptyList<DetailsItem>()) }
		)

		`when`(remoteDataSource.getMovieDetails(expectedMovieId)).thenReturn(
			flow {
				emit(MovieDetailResponse(id = expectedMovieId))
			}
		)

		runBlocking {
			repository.getMovieDetails(expectedMovieId).collect {
				if (it is ResultStatus.Success) {
					assertEquals(expectedMovieId, it.data.id)
				}
			}
		}
	}

	@Test
	fun getMovieDetailsFromDb() {
		`when`(localDataSource.getDetails(expectedMovieId)).thenReturn(
			flow {
				emit(
					listOf(DetailsItem(expectedMovieId, "", "", "", "", 0.0, "", 0, ""))
				)
			}
		)

		runBlocking {
			repository.getMovieDetails(expectedMovieId).collect {
				if (it is ResultStatus.Success) {
					assertEquals(expectedMovieId, it.data.id)
				}
			}
		}
	}

	@Test
	fun isFavoriteMovie() {
		runBlocking {
			repository.isFavoriteMovie(expectedMovieId).collect { actualIsFavorite ->
				assertEquals(expectedIsFavorite, actualIsFavorite)
			}
		}
	}

	@Test
	fun showFavoriteMovies() {
		val result = repository.showFavoriteMovies().getOrAwaitValue()
		assertEquals(5, result.size)
	}
}