package id.ak.movieshightlight.presentation.discover

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ak.movieshightlight.databinding.ListMovieGridBinding
import id.ak.movieshightlight.domain.ui.DiscoverItem
import id.ak.movieshightlight.presentation.details.DetailsActivity

class DiscoverAdapter(
	private val context: Context,
	private val items: List<DiscoverItem>,
	private val isMovie: Boolean
) : RecyclerView.Adapter<DiscoverAdapter.DiscoverViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DiscoverViewHolder(
		ListMovieGridBinding.inflate(LayoutInflater.from(context), parent, false)
	)

	override fun onBindViewHolder(holder: DiscoverViewHolder, position: Int) {
		holder.bindItem(items[position])
	}

	override fun getItemCount(): Int = items.size

	inner class DiscoverViewHolder(private val binding: ListMovieGridBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun bindItem(item: DiscoverItem) {
			with(binding) {
				Glide.with(context)
					.load(item.posterUrl)
					.into(ivPoster)

				tvTitle.text = item.title
				rating.rating = item.rating.toFloat() / 2
				tvRating.text = item.rating.toString()

				root.setOnClickListener {
					val i = Intent(context, DetailsActivity::class.java).apply {
						putExtra("id", item.id)
						putExtra("isMovie", isMovie)
					}
					context.startActivity(i)
				}
			}
		}
	}
}