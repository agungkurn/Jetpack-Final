package id.ak.movieshightlight.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import id.ak.movieshightlight.domain.ui.DiscoverItem

@Entity(tableName = "favorite")
data class FavoriteItem(
	@PrimaryKey
	val id: Int,

	val bannerUrl: String,

	val posterUrl: String,

	val title: String,

	val year: String,

	val rating: Double,

	val overview: String,

	val language: String,

	val runtime: Int,

	val isMovie: Boolean
)

