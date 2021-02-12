package id.ak.movieshightlight.presentation.discover

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.ak.movieshightlight.domain.IMovieRepository
import id.ak.movieshightlight.domain.ITvRepository
import id.ak.movieshightlight.domain.ResultStatus
import id.ak.movieshightlight.domain.ui.DetailsItem
import id.ak.movieshightlight.domain.ui.DiscoverItem
import id.ak.movieshightlight.getOrAwaitValue
import kotlinx.coroutines.flow.flow
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
class MainViewModelTest {
	private lateinit var viewModel: MainViewModel

	@get:Rule
	var rule = InstantTaskExecutorRule()

	@get:Rule
	val lenientRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.LENIENT)

	@Mock
	private lateinit var movieRepository: IMovieRepository

	@Mock
	private lateinit var tvRepository: ITvRepository

	@Mock
	private lateinit var movieObserver: Observer<ResultStatus<List<DiscoverItem>>>

	@Mock
	private lateinit var tvObserver: Observer<ResultStatus<List<DiscoverItem>>>

	private val dummyMovie = ResultStatus.Success(
		listOf(DiscoverItem(1, "", "Avengers: Endgame", 7.8))
	)

	private val dummyTv = ResultStatus.Success(
		listOf(DiscoverItem(2, "", "WandaVision", 8.8))
	)

	@Before
	fun setUp() {
		viewModel = MainViewModel(movieRepository, tvRepository)
		`when`(movieRepository.getMovies()).thenReturn(flow { emit(dummyMovie) })
		`when`(tvRepository.getTvShows()).thenReturn(flow { emit(dummyTv) })
	}

	@Test
	fun getMovies() {
		viewModel.getMovies()
		val value = viewModel.movies.getOrAwaitValue()
		if (value is ResultStatus.Success) {
			assert(value.data.isNotEmpty())
		}

		viewModel.movies.observeForever(movieObserver)
		verify(movieObserver).onChanged(dummyMovie)
	}

	@Test
	fun getTvShows() {
		viewModel.getTvShows()
		val value = viewModel.tvShows.getOrAwaitValue()
		if (value is ResultStatus.Success) {
			assert(value.data.isNotEmpty())
		}

		viewModel.tvShows.observeForever(tvObserver)
		verify(tvObserver).onChanged(dummyTv)
	}
}