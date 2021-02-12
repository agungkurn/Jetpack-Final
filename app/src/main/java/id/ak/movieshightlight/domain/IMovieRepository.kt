package id.ak.movieshightlight.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import id.ak.movieshightlight.domain.ui.DetailsItem
import id.ak.movieshightlight.domain.ui.DiscoverItem
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
	fun getMovies(): Flow<ResultStatus<List<DiscoverItem>>>
	fun getMovieDetails(id: Int): Flow<ResultStatus<DetailsItem>>
	fun isFavoriteMovie(id: Int): Flow<Boolean>
	suspend fun addToFavorite(item: DetailsItem)
	suspend fun removeFromFavorite(id: Int)
	fun showFavoriteMovies(): LiveData<PagedList<DiscoverItem>>
}