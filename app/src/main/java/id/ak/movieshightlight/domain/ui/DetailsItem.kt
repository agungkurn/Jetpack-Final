package id.ak.movieshightlight.domain.ui

import id.ak.movieshightlight.BuildConfig
import id.ak.movieshightlight.data.api.MovieDetailResponse
import id.ak.movieshightlight.data.api.TvShowDetailResponse

data class DetailsItem(
	val id: Int,
	val bannerUrl: String,
	val posterUrl: String,
	val title: String,
	val year: String,
	val rating: Double,
	val language:String,
	val runtime:Int,
	val overview: String,
)

fun MovieDetailResponse.toDetailsItem() = DetailsItem(
	id,
	bannerUrl = BuildConfig.BASE_IMAGE_URL + backdropPath,
	posterUrl = BuildConfig.BASE_IMAGE_URL + posterPath,
	title = title,
	year = if (releaseDate.isEmpty()) "-" else releaseDate.substring(0..3),
	rating = voteAverage,
	overview = overview,
	language = originalLanguage,
	runtime = runtime,
)

fun TvShowDetailResponse.toDetailsItem() = DetailsItem(
	id,
	bannerUrl = BuildConfig.BASE_IMAGE_URL + backdropPath,
	posterUrl = BuildConfig.BASE_IMAGE_URL + posterPath,
	title = name,
	year = if (firstAirDate.isEmpty()) "-" else firstAirDate.substring(0..3),
	rating = voteAverage,
	overview = overview,
	language = originalLanguage,
	runtime = episodeRunTime.last(),
)
