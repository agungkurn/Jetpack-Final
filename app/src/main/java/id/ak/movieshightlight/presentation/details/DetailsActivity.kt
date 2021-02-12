package id.ak.movieshightlight.presentation.details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import id.ak.movieshightlight.R
import id.ak.movieshightlight.databinding.ActivityDetailsBinding
import id.ak.movieshightlight.domain.ResultStatus
import id.ak.movieshightlight.domain.ui.DetailsItem
import id.ak.movieshightlight.service.Injection
import id.ak.movieshightlight.service.api.ApiFactory
import id.ak.movieshightlight.utils.EspressoIdlingResource
import id.ak.movieshightlight.utils.disable
import id.ak.movieshightlight.utils.enable

class DetailsActivity : AppCompatActivity() {

	private val binding by lazy {
		ActivityDetailsBinding.inflate(layoutInflater)
	}

	private val viewModel by viewModels<DetailsViewModel> {
		DetailsViewModel.getViewModelFactory(
			Injection.provideMovieRepository(ApiFactory.apiService, applicationContext),
			Injection.provideTvRepository(ApiFactory.apiService, applicationContext)
		)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		setSupportActionBar(binding.toolbarDetails)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.title = ""

		intent?.extras?.let {
			val id = it.getInt("id", 0)
			val isMovie = it.getBoolean("isMovie", true)

			viewModel.setIdAndIsMovie(id, isMovie)
		}

		registerObserver()
	}

	private fun registerObserver() {
		viewModel.details.observe(this) {
			when (it) {
				is ResultStatus.Loading -> {
					with(binding) {
						btnFavorite.disable()
					}
					EspressoIdlingResource.increment()
				}
				is ResultStatus.Success -> {
					binding.btnFavorite.enable()
					showData(it.data)
					if (!EspressoIdlingResource.idlingResource().isIdleNow) {
						EspressoIdlingResource.decrement()
					}
				}
				is ResultStatus.Failed -> {
					with(binding) {
						btnFavorite.disable()
					}
					Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_INDEFINITE)
						.setAction("Try again") {
							viewModel.fetchDetails()
						}.show()
				}
			}
		}
	}

	private fun showData(item: DetailsItem) {
		with(binding) {
			Glide.with(this@DetailsActivity)
				.load(item.bannerUrl)
				.into(ivBanner)

			Glide.with(this@DetailsActivity)
				.load(item.posterUrl)
				.into(ivPoster)

			tvTitle.text = item.title
			tvYear.text = item.year
			rating.rating = (item.rating / 10.0).toFloat()
			tvRating.text = "${item.rating}/10"
			tvLanguage.text = item.language.toUpperCase(java.util.Locale.ROOT)
			tvRuntime.text = "${item.runtime}"
			tvOverview.text = item.overview

			viewModel.isFavorite.observe(this@DetailsActivity) { isFavorite ->
				if (isFavorite) {
					btnFavorite.icon = ContextCompat.getDrawable(
						this@DetailsActivity, R.drawable.ic_baseline_favorite_24
					)
					btnFavorite.text = getString(R.string.remove_favorite)
					btnFavorite.setOnClickListener {
						viewModel.removeFromFavorite()
						Snackbar.make(
							binding.root,
							getString(R.string.success_remove_favorite),
							Snackbar.LENGTH_LONG
						).show()
					}
				} else {
					btnFavorite.icon = ContextCompat.getDrawable(
						this@DetailsActivity, R.drawable.ic_baseline_favorite_border_24
					)
					btnFavorite.text = getString(R.string.add_as_favorite)
					btnFavorite.setOnClickListener {
						viewModel.addToFavorite(item)
						Snackbar.make(
							binding.root,
							getString(R.string.success_add_favorite),
							Snackbar.LENGTH_LONG
						).show()
					}
				}
			}
		}
	}
}