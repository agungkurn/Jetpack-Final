package id.ak.movieshightlight.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import id.ak.movieshightlight.data.local.FavoriteItem
import id.ak.movieshightlight.domain.ui.DetailsItem
import id.ak.movieshightlight.domain.ui.DiscoverItem
import id.ak.movieshightlight.service.local.FavoriteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FilmLocalDataSource private constructor(private val favoriteDao: FavoriteDao) {
	companion object {
		@Volatile
		private var instance: FilmLocalDataSource? = null

		fun getInstance(favoriteDao: FavoriteDao): FilmLocalDataSource =
			instance ?: synchronized(this) {
				val newInstance = FilmLocalDataSource(favoriteDao)
				instance = newInstance
				newInstance
			}
	}

	fun getFavoriteMovies(): LiveData<PagedList<DiscoverItem>> {
		return favoriteDao.getFavoriteMovies().map {
			DiscoverItem(it.id, it.posterUrl, it.title, it.rating)
		}.toLiveData(5)
	}

	fun getFavoriteTvShows(): LiveData<PagedList<DiscoverItem>> {
		return favoriteDao.getFavoriteTvShows().map {
			DiscoverItem(it.id, it.posterUrl, it.title, it.rating)
		}.toLiveData(5)
	}

	fun isFavorite(id: Int): Flow<Boolean> {
		return favoriteDao.checkIfFavorite(id).map {
			it != 0
		}
	}

	suspend fun addToFavorite(item: FavoriteItem) {
		favoriteDao.addToFavorite(item)
	}

	suspend fun removeFromFavorite(id: Int) {
		favoriteDao.removeFromFavorite(id)
	}

	fun getDetails(id: Int): Flow<List<DetailsItem>> {
		return favoriteDao.getDetails(id).map {
			if (it.isNotEmpty()) {
				it.map { item ->
					DetailsItem(
						id = item.id,
						bannerUrl = item.bannerUrl,
						posterUrl = item.posterUrl,
						title = item.title,
						year = item.year,
						rating = item.rating,
						overview = item.overview,
						language = item.language,
						runtime = item.runtime,
					)
				}
			} else {
				emptyList()
			}
		}
	}
}