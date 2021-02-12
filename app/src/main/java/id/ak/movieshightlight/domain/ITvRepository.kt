package id.ak.movieshightlight.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import id.ak.movieshightlight.domain.ui.DetailsItem
import id.ak.movieshightlight.domain.ui.DiscoverItem
import kotlinx.coroutines.flow.Flow

interface ITvRepository {
	fun getTvShows(): Flow<ResultStatus<List<DiscoverItem>>>
	fun getTvShowDetails(id: Int): Flow<ResultStatus<DetailsItem>>
	fun isFavoriteTvShow(id: Int): Flow<Boolean>
	suspend fun addToFavorite(item: DetailsItem)
	suspend fun removeFromFavorite(id: Int)
	fun showFavoriteTvShows(): LiveData<PagedList<DiscoverItem>>
}