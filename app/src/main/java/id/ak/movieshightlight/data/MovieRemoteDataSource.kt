package id.ak.movieshightlight.data

import id.ak.movieshightlight.data.api.DiscoverMovieItem
import id.ak.movieshightlight.data.api.MovieDetailResponse
import id.ak.movieshightlight.service.api.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRemoteDataSource private constructor(private val apiService: ApiService) {
	companion object {
		@Volatile
		private var instance: MovieRemoteDataSource? = null

		fun getInstance(apiService: ApiService): MovieRemoteDataSource =
			instance ?: synchronized(this) {
				val newInstance = MovieRemoteDataSource(apiService)
				instance = newInstance
				newInstance
			}
	}

	fun getAllMovies(): Flow<List<DiscoverMovieItem>> = flow {
		val response = apiService.getAllMovies()

		response.results.filterNotNull().let {
			emit(it)
		}
	}

	fun getMovieDetails(id: Int): Flow<MovieDetailResponse> = flow {
		val response = apiService.getMovieDetails(id)
		emit(response)
	}
}