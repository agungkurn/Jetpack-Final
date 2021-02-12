package id.ak.movieshightlight.presentation.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import id.ak.movieshightlight.databinding.FragmentTvShowBinding
import id.ak.movieshightlight.domain.ResultStatus
import id.ak.movieshightlight.utils.EspressoIdlingResource

class TvShowFragment : Fragment() {

	private lateinit var binding: FragmentTvShowBinding
	private val viewModel by lazy {
		ViewModelProvider(requireActivity())[MainViewModel::class.java]
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		binding = FragmentTvShowBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.rvTv.layoutManager = GridLayoutManager(context, 2)
		binding.root.setOnRefreshListener {
			viewModel.getTvShows()
		}
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		binding.root.isRefreshing = true
		viewModel.getTvShows()

		viewModel.tvShows.observe(viewLifecycleOwner) {
			when (it) {
				is ResultStatus.Loading -> {
					binding.root.isRefreshing = true
					EspressoIdlingResource.increment()
				}
				is ResultStatus.Success -> {
					binding.root.isRefreshing = false
					binding.rvTv.adapter = DiscoverAdapter(requireContext(), it.data, false)
					if (!EspressoIdlingResource.idlingResource().isIdleNow) {
						EspressoIdlingResource.decrement()
					}
				}
				is ResultStatus.Failed -> {
					binding.root.isRefreshing = false
					Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_INDEFINITE)
						.setAction("Try again") {
							viewModel.getMovies()
						}.show()
				}
			}
		}
	}
}