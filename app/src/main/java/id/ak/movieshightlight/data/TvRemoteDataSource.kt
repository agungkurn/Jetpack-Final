package id.ak.movieshightlight.data

import id.ak.movieshightlight.data.api.DiscoverTvItem
import id.ak.movieshightlight.data.api.TvShowDetailResponse
import id.ak.movieshightlight.service.api.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TvRemoteDataSource private constructor(private val apiService: ApiService) {
	companion object {
		@Volatile
		private var instance: TvRemoteDataSource? = null

		fun getInstance(apiService: ApiService): TvRemoteDataSource =
			instance ?: synchronized(this) {
				val newInstance = TvRemoteDataSource(apiService)
				instance = newInstance
				newInstance
			}
	}

	fun getAllTvShows(): Flow<List<DiscoverTvItem>> = flow {
		val response = apiService.getAllTvShows()

		response.results.filterNotNull().let {
			emit(it)
		}
	}

	fun getTvShowDetails(id: Int): Flow<TvShowDetailResponse> = flow {
		val response = apiService.getTvShowDetails(id)
		emit(response)
	}
}