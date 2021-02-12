package id.ak.movieshightlight.domain.ui

import id.ak.movieshightlight.BuildConfig
import id.ak.movieshightlight.data.api.DiscoverMovieItem
import id.ak.movieshightlight.data.api.DiscoverTvItem

data class DiscoverItem(
	val id: Int,
	val posterUrl: String,
	val title: String,
	val rating: Double
)

fun List<DiscoverMovieItem>.movieToDiscoverItem() = map {
	DiscoverItem(
		id = it.id ?: 0,
		posterUrl = if (it.posterPath != null) {
			BuildConfig.BASE_IMAGE_URL + it.posterPath
		} else {
			""
		},
		title = it.title ?: "",
		rating = it.voteAverage ?: 0.0
	)
}

fun List<DiscoverTvItem>.tvToDiscoverItem() = map {
	DiscoverItem(
		id = it.id ?: 0,
		posterUrl = if (it.posterPath != null) {
			BuildConfig.BASE_IMAGE_URL + it.posterPath
		} else {
			""
		},
		title = it.name ?: "",
		rating = it.voteAverage ?: 0.0
	)
}
