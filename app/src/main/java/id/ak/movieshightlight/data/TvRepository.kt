package id.ak.movieshightlight.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import id.ak.movieshightlight.data.local.FavoriteItem
import id.ak.movieshightlight.domain.ITvRepository
import id.ak.movieshightlight.domain.ResultStatus
import id.ak.movieshightlight.domain.ui.DetailsItem
import id.ak.movieshightlight.domain.ui.DiscoverItem
import id.ak.movieshightlight.domain.ui.toDetailsItem
import id.ak.movieshightlight.domain.ui.tvToDiscoverItem
import id.ak.movieshightlight.utils.debug
import id.ak.movieshightlight.utils.error
import kotlinx.coroutines.flow.*
import java.net.ConnectException

class TvRepository private constructor(
	private val remoteDataSource: TvRemoteDataSource,
	private val localDataSource: FilmLocalDataSource
) : ITvRepository {

	companion object {
		@Volatile
		private var instance: TvRepository? = null

		fun getInstance(
			remoteDataSource: TvRemoteDataSource, localDataSource: FilmLocalDataSource
		): TvRepository =
			instance ?: synchronized(this) {
				val newInstance = TvRepository(remoteDataSource, localDataSource)
				instance = newInstance
				newInstance
			}
	}

	override fun getTvShows(): Flow<ResultStatus<List<DiscoverItem>>> {
		return flow {
			emit(ResultStatus.Loading)

			try {
				remoteDataSource.getAllTvShows().collect {
					emit(ResultStatus.Success(it.tvToDiscoverItem()))
				}
			} catch (e: ConnectException) {
				error(e.localizedMessage, this@TvRepository)
				emit(ResultStatus.Failed(message = "Connection error"))
			} catch (e: Exception) {
				error(e.toString(), this@TvRepository)
				emit(ResultStatus.Failed(exception = e, message = "An error occurred"))
			}
		}
	}

	override fun getTvShowDetails(id: Int): Flow<ResultStatus<DetailsItem>> {
		return flow {
			emit(ResultStatus.Loading)

			try {
				val data = localDataSource.getDetails(id).first()
				debug(data.toString(), this@TvRepository)
				if (data.isEmpty()) {
					remoteDataSource.getTvShowDetails(id).collect { response ->
						emit(ResultStatus.Success(response.toDetailsItem()))
					}
				} else {
					emit(ResultStatus.Success(data.first()))
				}
			} catch (e: ConnectException) {
				error(e.localizedMessage, this@TvRepository)
				emit(ResultStatus.Failed(message = "Connection error"))
			} catch (e: Exception) {
				error(e.toString(), this@TvRepository)
				emit(ResultStatus.Failed(exception = e, message = "An error occurred"))
			}
		}
	}

	override fun isFavoriteTvShow(id: Int): Flow<Boolean> {
		return localDataSource.isFavorite(id)
	}

	override suspend fun addToFavorite(item: DetailsItem) {
		val fav = FavoriteItem(
			item.id,
			item.bannerUrl,
			item.posterUrl,
			item.title,
			item.year,
			item.rating,
			item.overview,
			item.language,
			item.runtime,
			false
		)
		localDataSource.addToFavorite(fav)
	}

	override suspend fun removeFromFavorite(id: Int) {
		localDataSource.removeFromFavorite(id)
	}

	override fun showFavoriteTvShows(): LiveData<PagedList<DiscoverItem>> {
		return localDataSource.getFavoriteTvShows()
	}
}