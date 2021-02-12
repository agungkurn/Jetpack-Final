package id.ak.movieshightlight.presentation.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import id.ak.movieshightlight.R
import id.ak.movieshightlight.databinding.FragmentFavoriteBinding
import id.ak.movieshightlight.domain.ui.DiscoverItem
import id.ak.movieshightlight.service.Injection
import id.ak.movieshightlight.service.api.ApiFactory
import id.ak.movieshightlight.utils.EspressoIdlingResource
import id.ak.movieshightlight.utils.gone
import id.ak.movieshightlight.utils.invisible
import id.ak.movieshightlight.utils.visible

class FavoriteFragment : Fragment() {

	private lateinit var binding: FragmentFavoriteBinding

	private lateinit var movieAdapter: FavoriteAdapter
	private lateinit var tvAdapter: FavoriteAdapter

	private val viewModel by lazy {
		ViewModelProvider(
			requireActivity(), FavoriteViewModel.getViewModelFactory(
				Injection.provideMovieRepository(
					ApiFactory.apiService, requireActivity().applicationContext
				),
				Injection.provideTvRepository(
					ApiFactory.apiService, requireActivity().applicationContext
				)
			)
		)[FavoriteViewModel::class.java]
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		binding = FragmentFavoriteBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		movieAdapter = FavoriteAdapter(requireActivity(), true)
		tvAdapter = FavoriteAdapter(requireActivity(), false)

		EspressoIdlingResource.increment()

		registerObserver()
	}

	private fun registerObserver() {
		viewModel.favoriteMovies.observe(viewLifecycleOwner) {
			binding.placeholderMovies.invisible()

			binding.headerMovies.text = "${getString(R.string.title_movie)} (${it.size})"

			if (it.isNotEmpty()) {
				binding.rvMovies.visible()
				showFavoriteMovies(it)
			} else {
				binding.rvMovies.gone()
			}

			if (!EspressoIdlingResource.idlingResource().isIdleNow){
				EspressoIdlingResource.decrement()
			}
		}

		viewModel.favoriteTvShows.observe(viewLifecycleOwner) {
			binding.placeholderTv.invisible()

			binding.headerTv.text = "${getString(R.string.title_tv_show)} (${it.size})"

			if (it.isNotEmpty()) {
				binding.rvTv.visible()
				showFavoriteTvShows(it)
			} else {
				binding.rvTv.gone()
			}

			if (!EspressoIdlingResource.idlingResource().isIdleNow){
				EspressoIdlingResource.decrement()
			}
		}
	}

	private fun showFavoriteMovies(items: PagedList<DiscoverItem>) {
		movieAdapter.submitList(items)
		binding.rvMovies.adapter = movieAdapter
	}

	private fun showFavoriteTvShows(items: PagedList<DiscoverItem>) {
		tvAdapter.submitList(items)
		binding.rvTv.adapter = tvAdapter
	}
}