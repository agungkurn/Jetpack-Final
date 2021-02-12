package id.ak.movieshightlight.presentation.details

import androidx.lifecycle.*
import id.ak.movieshightlight.domain.IMovieRepository
import id.ak.movieshightlight.domain.ITvRepository
import id.ak.movieshightlight.domain.ResultStatus
import id.ak.movieshightlight.domain.ui.DetailsItem
import id.ak.movieshightlight.utils.debug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailsViewModel(
	private val movieRepository: IMovieRepository, private val tvRepository: ITvRepository
) : ViewModel() {

	companion object {
		fun getViewModelFactory(movieRepository: IMovieRepository, tvRepository: ITvRepository) =
			object : ViewModelProvider.Factory {
				override fun <T : ViewModel?> create(modelClass: Class<T>): T {
					if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
						return DetailsViewModel(movieRepository, tvRepository) as T
					}
					throw ClassCastException("invalid viewmodel")
				}
			}
	}

	private lateinit var idAndIsMovie: Pair<Int, Boolean>

	fun setIdAndIsMovie(id: Int, isMovie: Boolean) {
		idAndIsMovie = Pair(id, isMovie)
		fetchDetails()
		checkIfFavorite()
	}

	private val _details = MutableLiveData<ResultStatus<DetailsItem>>()
	val details: LiveData<ResultStatus<DetailsItem>> = _details

	fun fetchDetails() {
		val id = idAndIsMovie.first
		val isMovie = idAndIsMovie.second

		viewModelScope.launch(Dispatchers.IO) {
			if (isMovie) {
				movieRepository.getMovieDetails(id).collect {
					_details.postValue(it)
				}
			} else {
				tvRepository.getTvShowDetails(id).collect {
					_details.postValue(it)
				}
			}
		}
	}

	val isFavorite = MediatorLiveData<Boolean>()

	private fun checkIfFavorite() {
		val id = idAndIsMovie.first
		val isMovie = idAndIsMovie.second

		if (isMovie) {
			isFavorite.addSource(movieRepository.isFavoriteMovie(id).asLiveData(Dispatchers.IO)) {
				isFavorite.value = it
			}
		} else {
			isFavorite.addSource(tvRepository.isFavoriteTvShow(id).asLiveData(Dispatchers.IO)) {
				isFavorite.value = it
			}
		}
	}

	fun addToFavorite(item: DetailsItem) {
		val isMovie = idAndIsMovie.second

		viewModelScope.launch {
			if (isMovie) {
				debug("movie added",this@DetailsViewModel)
				movieRepository.addToFavorite(item)
			} else {
				debug("tv added",this@DetailsViewModel)
				tvRepository.addToFavorite(item)
			}
		}
	}

	fun removeFromFavorite() {
		val id = idAndIsMovie.first
		val isMovie = idAndIsMovie.second

		viewModelScope.launch {
			if (isMovie) {
				movieRepository.removeFromFavorite(id)
			} else {
				tvRepository.removeFromFavorite(id)
			}
		}
	}
}