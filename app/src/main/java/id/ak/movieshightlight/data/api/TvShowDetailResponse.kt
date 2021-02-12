package id.ak.movieshightlight.data.api

import com.google.gson.annotations.SerializedName

data class TvShowDetailResponse(

	@field:SerializedName("original_language")
	val originalLanguage: String = "",

	@field:SerializedName("number_of_episodes")
	val numberOfEpisodes: Int = 0,

	@field:SerializedName("type")
	val type: String = "",

	@field:SerializedName("backdrop_path")
	val backdropPath: String = "",

	@field:SerializedName("genres")
	val genres: List<GenresItem?>? = null,

	@field:SerializedName("popularity")
	val popularity: Double = 0.0,

	@field:SerializedName("id")
	val id: Int = 0,

	@field:SerializedName("number_of_seasons")
	val numberOfSeasons: Int = 0,

	@field:SerializedName("first_air_date")
	val firstAirDate: String = "",

	@field:SerializedName("overview")
	val overview: String = "",

	@field:SerializedName("languages")
	val languages: List<String?>? = null,

	@field:SerializedName("last_episode_to_air")
	val lastEpisodeToAir: EpisodeToAir? = null,

	@field:SerializedName("poster_path")
	val posterPath: String = "",

	@field:SerializedName("origin_country")
	val originCountry: List<String?>? = null,

	@field:SerializedName("production_companies")
	val productionCompanies: List<ProductionCompaniesItem?>? = null,

	@field:SerializedName("original_name")
	val originalName: String = "",

	@field:SerializedName("vote_average")
	val voteAverage: Double = 0.0,

	@field:SerializedName("name")
	val name: String = "",

	@field:SerializedName("tagline")
	val tagline: String = "",

	@field:SerializedName("episode_run_time")
	val episodeRunTime: List<Int> = emptyList(),

	@field:SerializedName("next_episode_to_air")
	val nextEpisodeToAir: EpisodeToAir? = null,

	@field:SerializedName("in_production")
	val inProduction: Boolean? = null,

	@field:SerializedName("last_air_date")
	val lastAirDate: String = "",

	@field:SerializedName("status")
	val status: String = ""
)

data class EpisodeToAir(

	@field:SerializedName("production_code")
	val productionCode: String = "",

	@field:SerializedName("air_date")
	val airDate: String = "",

	@field:SerializedName("overview")
	val overview: String = "",

	@field:SerializedName("episode_number")
	val episodeNumber: Int = 0,

	@field:SerializedName("vote_average")
	val voteAverage: Double = 0.0,

	@field:SerializedName("name")
	val name: String = "",

	@field:SerializedName("season_number")
	val seasonNumber: Int = 0,

	@field:SerializedName("id")
	val id: Int = 0,

	@field:SerializedName("still_path")
	val stillPath: String = "",

	@field:SerializedName("vote_count")
	val voteCount: Int = 0
)