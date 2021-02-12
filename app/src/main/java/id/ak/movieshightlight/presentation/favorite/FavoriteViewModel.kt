package id.ak.movieshightlight.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ak.movieshightlight.domain.IMovieRepository
import id.ak.movieshightlight.domain.ITvRepository

class FavoriteViewModel(movieRepository: IMovieRepository, tvRepository: ITvRepository) :
	ViewModel() {

	companion object {
		fun getViewModelFactory(movieRepository: IMovieRepository, tvRepository: ITvRepository) =
			object : ViewModelProvider.Factory {
				override fun <T : ViewModel?> create(modelClass: Class<T>): T {
					if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
						return FavoriteViewModel(movieRepository, tvRepository) as T
					}
					throw ClassCastException("invalid viewmodel")
				}
			}
	}

	val favoriteMovies = movieRepository.showFavoriteMovies()

	val favoriteTvShows = tvRepository.showFavoriteTvShows()
}