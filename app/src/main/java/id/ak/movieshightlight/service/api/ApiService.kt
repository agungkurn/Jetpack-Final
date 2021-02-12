package id.ak.movieshightlight.service.api

import id.ak.movieshightlight.BuildConfig
import id.ak.movieshightlight.data.api.DiscoverMovieResponse
import id.ak.movieshightlight.data.api.DiscoverTvShowResponse
import id.ak.movieshightlight.data.api.MovieDetailResponse
import id.ak.movieshightlight.data.api.TvShowDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
	@GET("discover/movie")
	suspend fun getAllMovies(
		@Query("api_key") apiKey: String = BuildConfig.API_KEY
	): DiscoverMovieResponse

	@GET("discover/tv")
	suspend fun getAllTvShows(
		@Query("api_key") apiKey: String = BuildConfig.API_KEY
	): DiscoverTvShowResponse

	@GET("movie/{id}")
	suspend fun getMovieDetails(
		@Path("id") movieId: Int,
		@Query("api_key") apiKey: String = BuildConfig.API_KEY
	): MovieDetailResponse

	@GET("tv/{id}")
	suspend fun getTvShowDetails(
		@Path("id") tvShowId: Int,
		@Query("api_key") apiKey: String = BuildConfig.API_KEY
	): TvShowDetailResponse
}