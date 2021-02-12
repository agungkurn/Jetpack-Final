package id.ak.movieshightlight.service

import android.content.Context
import id.ak.movieshightlight.data.*
import id.ak.movieshightlight.service.api.ApiService
import id.ak.movieshightlight.service.local.FavoriteDatabase

object Injection {
	fun provideMovieRepository(
		apiService: ApiService, applicationContext: Context
	): MovieRepository {
		return MovieRepository.getInstance(
			MovieRemoteDataSource.getInstance(apiService),
			provideLocalDataSource(applicationContext)
		)
	}

	fun provideTvRepository(apiService: ApiService, applicationContext: Context): TvRepository {
		return TvRepository.getInstance(
			TvRemoteDataSource.getInstance(apiService),
			provideLocalDataSource(applicationContext)
		)
	}

	private fun provideLocalDataSource(applicationContext: Context): FilmLocalDataSource {
		return FilmLocalDataSource.getInstance(
			FavoriteDatabase.getInstance(applicationContext).favoriteDao()
		)
	}
}