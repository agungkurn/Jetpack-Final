package id.ak.movieshightlight.presentation.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import id.ak.movieshightlight.domain.IMovieRepository
import id.ak.movieshightlight.domain.ITvRepository
import id.ak.movieshightlight.domain.ui.DiscoverItem
import id.ak.movieshightlight.getOrAwaitValue
import org.junit.Assert.assertEquals
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
class FavoriteViewModelTest {
	private lateinit var viewModel: FavoriteViewModel

	@get:Rule
	var rule = InstantTaskExecutorRule()

	@get:Rule
	val lenientRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.LENIENT)

	@Mock
	private lateinit var movieRepository: IMovieRepository

	@Mock
	private lateinit var tvRepository: ITvRepository

	@Mock
	private lateinit var pagedList: PagedList<DiscoverItem>

	@Mock
	private lateinit var movieObserver: Observer<PagedList<DiscoverItem>>

	@Mock
	private lateinit var tvObserver: Observer<PagedList<DiscoverItem>>

	@Before
	fun setUp() {
		`when`(pagedList.size).thenReturn(5)
		`when`(movieRepository.showFavoriteMovies()).thenReturn(MutableLiveData(pagedList))
		`when`(tvRepository.showFavoriteTvShows()).thenReturn(MutableLiveData(pagedList))

		viewModel = FavoriteViewModel(movieRepository, tvRepository)
	}

	@Test
	fun getFavoriteMovies() {
		val result = viewModel.favoriteMovies.getOrAwaitValue()

		assertEquals(5, result.size)
		verify(movieRepository).showFavoriteMovies()

		viewModel.favoriteMovies.observeForever(movieObserver)
		verify(movieObserver).onChanged(pagedList)
	}

	@Test
	fun getFavoriteTvShows() {
		val result = viewModel.favoriteTvShows.getOrAwaitValue()

		assertEquals(5, result.size)
		verify(tvRepository).showFavoriteTvShows()

		viewModel.favoriteTvShows.observeForever(tvObserver)
		verify(tvObserver).onChanged(pagedList)
	}
}