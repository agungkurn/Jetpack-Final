package id.ak.movieshightlight.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import id.ak.movieshightlight.data.local.FavoriteItem
import id.ak.movieshightlight.domain.IMovieRepository
import id.ak.movieshightlight.domain.ResultStatus
import id.ak.movieshightlight.domain.ui.DetailsItem
import id.ak.movieshightlight.domain.ui.DiscoverItem
import id.ak.movieshightlight.domain.ui.movieToDiscoverItem
import id.ak.movieshightlight.domain.ui.toDetailsItem
import id.ak.movieshightlight.utils.debug
import id.ak.movieshightlight.utils.error
import kotlinx.coroutines.flow.*
import java.net.ConnectException

class MovieRepository private constructor(
	private val remoteDataSource: MovieRemoteDataSource,
	private val localDataSource: FilmLocalDataSource
) : IMovieRepository {

	companion object {
		@Volatile
		private var instance: MovieRepository? = null

		fun getInstance(
			remoteDataSource: MovieRemoteDataSource, localDataSource: FilmLocalDataSource
		): MovieRepository =
			instance ?: synchronized(this) {
				val newInstance = MovieRepository(remoteDataSource, localDataSource)
				instance = newInstance
				newInstance
			}
	}

	override fun getMovies(): Flow<ResultStatus<List<DiscoverItem>>> {
		return flow {
			emit(ResultStatus.Loading)

			try {
				remoteDataSource.getAllMovies().collect {
					emit(ResultStatus.Success(it.movieToDiscoverItem()))
				}
			} catch (e: ConnectException) {
				error(e.localizedMessage, this@MovieRepository)
				emit(ResultStatus.Failed(message = "Connection error"))
			} catch (e: Exception) {
				e.printStackTrace()
				emit(ResultStatus.Failed(exception = e, message = "An error occurred"))
			}
		}
	}

	override fun getMovieDetails(id: Int): Flow<ResultStatus<DetailsItem>> {
		return flow {
			emit(ResultStatus.Loading)

			try {
				val data = localDataSource.getDetails(id).first()
				debug(data.toString(), this@MovieRepository)
				if (data.isEmpty()) {
					remoteDataSource.getMovieDetails(id).collect { response ->
						emit(ResultStatus.Success(response.toDetailsItem()))
					}
				} else {
					emit(ResultStatus.Success(data.first()))
				}
			} catch (e: ConnectException) {
				error(e.localizedMessage, this@MovieRepository)
				emit(ResultStatus.Failed(message = "Connection error"))
			} catch (e: Exception) {
				error(e.localizedMessage, this@MovieRepository)
				emit(ResultStatus.Failed(exception = e, message = "An error occurred"))
			}
		}
	}

	override fun isFavoriteMovie(id: Int): Flow<Boolean> {
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
			true
		)
		localDataSource.addToFavorite(fav)
	}

	override suspend fun removeFromFavorite(id: Int) {
		localDataSource.removeFromFavorite(id)
	}

	override fun showFavoriteMovies(): LiveData<PagedList<DiscoverItem>> {
		return localDataSource.getFavoriteMovies()
	}
}