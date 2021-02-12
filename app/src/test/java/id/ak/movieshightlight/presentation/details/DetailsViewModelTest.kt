package id.ak.movieshightlight.presentation.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.ak.movieshightlight.domain.IMovieRepository
import id.ak.movieshightlight.domain.ITvRepository
import id.ak.movieshightlight.domain.ResultStatus
import id.ak.movieshightlight.domain.ui.DetailsItem
import id.ak.movieshightlight.getOrAwaitValue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness

@RunWith(MockitoJUnitRunner.Silent::class)
class DetailsViewModelTest {
	private lateinit var viewModel: DetailsViewModel

	@get:Rule
	val lenientRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.LENIENT)

	@get:Rule
	val rule = InstantTaskExecutorRule()

	@Mock
	private lateinit var movieRepository: IMovieRepository

	@Mock
	private lateinit var tvRepository: ITvRepository

	@Mock
	private lateinit var detailsObserver: Observer<ResultStatus<DetailsItem>>

	@Mock
	private lateinit var favoriteMovieObserver: Observer<Boolean>

	@Mock
	private lateinit var favoriteTvObserver: Observer<Boolean>

	private val dummyMovie = ResultStatus.Success(
		DetailsItem(
			id = 1,
			title = "Avengers: Endgame",
			bannerUrl = "",
			posterUrl = "",
			year = "",
			rating = 0.0,
			language = "",
			runtime = 0,
			overview = ""
		)
	)

	private val dummyTv = ResultStatus.Success(
		DetailsItem(
			id = 2,
			title = "WandaVision",
			bannerUrl = "",
			posterUrl = "",
			year = "",
			rating = 0.0,
			language = "",
			runtime = 0,
			overview = ""
		)
	)

	private val dummyIsFavorite = false

	@Before
	fun setUp() {
		viewModel = DetailsViewModel(movieRepository, tvRepository)

		`when`(movieRepository.getMovieDetails(1)).thenReturn(flow { emit(dummyMovie) })
		`when`(movieRepository.isFavoriteMovie(1)).thenReturn(flow { emit(dummyIsFavorite) })

		`when`(tvRepository.getTvShowDetails(2)).thenReturn(flow { emit(dummyTv) })
		`when`(tvRepository.isFavoriteTvShow(2)).thenReturn(flow { emit(dummyIsFavorite) })
	}

	@Test
	fun getMovieDetails() {
		viewModel.setIdAndIsMovie(1, true)

		val result = viewModel.details.getOrAwaitValue()
		// Make sure the actual data matches the expected data
		if (result is ResultStatus.Success) {
			assertEquals(1, result.data.id)
		}

		viewModel.details.observeForever(detailsObserver)
		verify(detailsObserver).onChanged(dummyMovie)
	}

	@Test
	fun getTvShowDetails() {
		viewModel.setIdAndIsMovie(2, false)

		val result = viewModel.details.getOrAwaitValue()
		// Make sure the actual data matches the expected data
		if (result is ResultStatus.Success) {
			assertEquals(2, result.data.id)
		}

		viewModel.details.observeForever(detailsObserver)
		verify(detailsObserver).onChanged(dummyTv)
	}

	@ExperimentalCoroutinesApi
	@Test
	fun checkIfFavoriteMovie() {
		Dispatchers.setMain(Dispatchers.IO)

		viewModel.isFavorite.observeForever(favoriteMovieObserver)
		viewModel.setIdAndIsMovie(1, true)
		verify(movieRepository).isFavoriteMovie(1)
		val favoriteActual = viewModel.isFavorite.getOrAwaitValue()
		// Make sure the actual data is not favorite item
		assertEquals(dummyIsFavorite, favoriteActual)

		verify(favoriteMovieObserver).onChanged(dummyIsFavorite)

		Dispatchers.resetMain()
	}

	@ExperimentalCoroutinesApi
	@Test
	fun checkIfFavoriteTvShow() {
		Dispatchers.setMain(Dispatchers.IO)

		viewModel.isFavorite.observeForever(favoriteTvObserver)
		viewModel.setIdAndIsMovie(2, false)
		val favoriteActual = viewModel.isFavorite.getOrAwaitValue()
		// Make sure the actual data is not favorite item
		assertEquals(dummyIsFavorite, favoriteActual)

		verify(favoriteTvObserver).onChanged(dummyIsFavorite)

		Dispatchers.resetMain()
	}

	@ExperimentalCoroutinesApi
	@Test
	fun removeMovieFromFavorite() {
		Dispatchers.setMain(Dispatchers.IO)

		viewModel.setIdAndIsMovie(1, true)

		val data = viewModel.details.getOrAwaitValue()
		if (data is ResultStatus.Success) {
			viewModel.removeFromFavorite()
			runBlocking {
				verify(movieRepository).removeFromFavorite(data.data.id)
			}
		}
		Dispatchers.resetMain()
	}

	@ExperimentalCoroutinesApi
	@Test
	fun removeTvShowFromFavorite() {
		Dispatchers.setMain(Dispatchers.IO)

		viewModel.setIdAndIsMovie(2, false)

		val data = viewModel.details.getOrAwaitValue()
		if (data is ResultStatus.Success) {
			viewModel.removeFromFavorite()
			runBlocking {
				verify(tvRepository).removeFromFavorite(data.data.id)
			}
		}
		Dispatchers.resetMain()
	}

	@ExperimentalCoroutinesApi
	@Test
	fun addMovieToFavorite() {
		Dispatchers.setMain(Dispatchers.IO)

		viewModel.setIdAndIsMovie(1, true)
		verify(movieRepository).getMovieDetails(1)

		viewModel.details.observeForever {
			if (it is ResultStatus.Success) {
				assertEquals(dummyMovie.data,it.data)
				viewModel.addToFavorite(it.data)
				runBlocking {
					verify(movieRepository).addToFavorite(it.data)
				}
			}
		}

//		runBlocking {
//
//			val data = viewModel.details.getOrAwaitValue()
//			if (data is ResultStatus.Success) {
//				viewModel.addToFavorite(data.data)
//				verify(movieRepository).addToFavorite(data.data)
//			}
//		}
		Dispatchers.resetMain()
	}
}