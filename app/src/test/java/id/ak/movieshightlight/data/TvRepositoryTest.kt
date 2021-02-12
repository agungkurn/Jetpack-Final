package id.ak.movieshightlight.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import id.ak.movieshightlight.data.api.DiscoverTvItem
import id.ak.movieshightlight.data.api.TvShowDetailResponse
import id.ak.movieshightlight.domain.ResultStatus
import id.ak.movieshightlight.domain.ui.DetailsItem
import id.ak.movieshightlight.domain.ui.DiscoverItem
import id.ak.movieshightlight.getOrAwaitValue
import junit.framework.TestCase
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
class TvRepositoryTest {
	@get:Rule
	val lenientRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.LENIENT)

	@get:Rule
	val rule = InstantTaskExecutorRule()

	private lateinit var repository: TvRepository

	@Mock
	private lateinit var remoteDataSource: TvRemoteDataSource

	@Mock
	private lateinit var localDataSource: FilmLocalDataSource

	@Mock
	private lateinit var favorites: PagedList<DiscoverItem>

	private val dummyList = listOf(DiscoverTvItem())

	private val expectedId = 1

	private val expectedIsFavorite = false

	@Before
	fun setUp() {
		repository = TvRepository.getInstance(remoteDataSource, localDataSource)

		`when`(remoteDataSource.getAllTvShows()).thenReturn(flow { emit(dummyList) })
		`when`(favorites.size).thenReturn(5)
		`when`(localDataSource.isFavorite(expectedId)).thenReturn(
			flow {
				emit(expectedIsFavorite)
			}
		)
		`when`(localDataSource.getFavoriteTvShows()).thenReturn(MutableLiveData(favorites))
	}

	@Test
	fun getTvShows() {
		runBlocking {
			repository.getTvShows().collect {
				if (it is ResultStatus.Success) {
					assert(it.data.isNotEmpty())
				}
			}
		}
	}

	@Test
	fun getTvShowDetailsFromApi() {
		`when`(localDataSource.getDetails(expectedId)).thenReturn(
			flow { emit(emptyList<DetailsItem>()) }
		)

		`when`(remoteDataSource.getTvShowDetails(expectedId)).thenReturn(
			flow {
				emit(TvShowDetailResponse(id = expectedId))
			}
		)

		runBlocking {
			repository.getTvShowDetails(expectedId).collect {
				if (it is ResultStatus.Success) {
					assertEquals(expectedId, it.data.id)
				}
			}
		}
	}

	@Test
	fun getTvShowDetailsFromDb() {
		`when`(localDataSource.getDetails(expectedId)).thenReturn(
			flow {
				emit(
					listOf(
						DetailsItem(expectedId, "", "", "", "", 0.0, "", 0, "")
					)
				)
			}
		)

		runBlocking {
			repository.getTvShowDetails(expectedId).collect {
				if (it is ResultStatus.Success) {
					assertEquals(expectedId, it.data.id)
				}
			}
		}
	}

	@Test
	fun isFavoriteTvShow() {
		runBlocking {
			repository.isFavoriteTvShow(expectedId).collect { actualIsFavorite ->
				assertEquals(expectedIsFavorite, actualIsFavorite)
			}
		}
	}

	@Test
	fun showFavoriteTvShows() {
		val result = repository.showFavoriteTvShows().getOrAwaitValue()
		assertEquals(5, result.size)
	}
}