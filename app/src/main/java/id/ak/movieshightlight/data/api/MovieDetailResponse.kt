package id.ak.movieshightlight.data.api

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(

	@field:SerializedName("original_language")
	val originalLanguage: String = "",

	@field:SerializedName("title")
	val title: String = "",

	@field:SerializedName("backdrop_path")
	val backdropPath: String = "",

	@field:SerializedName("genres")
	val genres: List<GenresItem?> = emptyList(),

	@field:SerializedName("popularity")
	val popularity: Double = 0.0,

	@field:SerializedName("id")
	val id: Int = 0,

	@field:SerializedName("overview")
	val overview: String = "",

	@field:SerializedName("original_title")
	val originalTitle: String = "",

	@field:SerializedName("runtime")
	val runtime: Int = 0,

	@field:SerializedName("poster_path")
	val posterPath: String = "",

	@field:SerializedName("production_companies")
	val productionCompanies: List<ProductionCompaniesItem?> = emptyList(),

	@field:SerializedName("release_date")
	val releaseDate: String = "",

	@field:SerializedName("vote_average")
	val voteAverage: Double = 0.0,

	@field:SerializedName("tagline")
	val tagline: String = "",

	@field:SerializedName("adult")
	val adult: Boolean = false,

	@field:SerializedName("status")
	val status: String = ""
)

data class GenresItem(

	@field:SerializedName("name")
	val name: String = "",

	@field:SerializedName("id")
	val id: Int = 0
)

data class ProductionCompaniesItem(

	@field:SerializedName("logo_path")
	val logoPath: Any? = null,

	@field:SerializedName("name")
	val name: String = "",

	@field:SerializedName("id")
	val id: Int = 0,

	@field:SerializedName("origin_country")
	val originCountry: String = ""
)
