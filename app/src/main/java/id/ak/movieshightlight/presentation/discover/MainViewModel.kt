package id.ak.movieshightlight.presentation.discover

import androidx.lifecycle.*
import id.ak.movieshightlight.domain.IMovieRepository
import id.ak.movieshightlight.domain.ITvRepository
import id.ak.movieshightlight.domain.ResultStatus
import id.ak.movieshightlight.domain.ui.DiscoverItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.ClassCastException

class MainViewModel(
	private val movieRepository: IMovieRepository, private val tvRepository: ITvRepository
) : ViewModel() {

	companion object {
		fun getViewModelFactory(movieRepository: IMovieRepository, tvRepository: ITvRepository) =
			object : ViewModelProvider.Factory {
				override fun <T : ViewModel?> create(modelClass: Class<T>): T {
					if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
						return MainViewModel(movieRepository, tvRepository) as T
					}
					throw ClassCastException("invalid viewmodel")
				}
			}
	}

	private val _movies = MutableLiveData<ResultStatus<List<DiscoverItem>>>()
	val movies: LiveData<ResultStatus<List<DiscoverItem>>> = _movies

	private val _tvShows = MutableLiveData<ResultStatus<List<DiscoverItem>>>()
	val tvShows: LiveData<ResultStatus<List<DiscoverItem>>> = _tvShows

	fun getMovies() {
		viewModelScope.launch(Dispatchers.IO) {
			movieRepository.getMovies().collect {
				_movies.postValue(it)
			}
		}
	}

	fun getTvShows() {
		viewModelScope.launch(Dispatchers.IO) {
			tvRepository.getTvShows().collect {
				_tvShows.postValue(it)
			}
		}
	}
}