package id.ak.movieshightlight.presentation.favorite

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ak.movieshightlight.databinding.ListFavoriteBinding
import id.ak.movieshightlight.domain.ui.DiscoverItem
import id.ak.movieshightlight.presentation.details.DetailsActivity

class FavoriteAdapter(private val activity: Activity, private val isMovie: Boolean) :
	PagedListAdapter<DiscoverItem, FavoriteAdapter.DiscoverViewHolder>(callback) {

	companion object {
		private val callback = object : DiffUtil.ItemCallback<DiscoverItem>() {
			override fun areItemsTheSame(oldItem: DiscoverItem, newItem: DiscoverItem): Boolean =
				oldItem.id == newItem.id

			override fun areContentsTheSame(oldItem: DiscoverItem, newItem: DiscoverItem): Boolean =
				oldItem == newItem

		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DiscoverViewHolder(
		ListFavoriteBinding.inflate(LayoutInflater.from(activity), parent, false)
	)

	override fun onBindViewHolder(holder: DiscoverViewHolder, position: Int) {
		holder.bindItem(getItem(position) as DiscoverItem)
	}

	inner class DiscoverViewHolder(private val binding: ListFavoriteBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun bindItem(item: DiscoverItem) {
			with(binding) {
				Glide.with(activity)
					.load(item.posterUrl)
					.into(ivPoster)

				tvTitle.text = item.title
				rating.rating = item.rating.toFloat() / 2
				tvRating.text = item.rating.toString()

				root.setOnClickListener {
					val i = Intent(activity, DetailsActivity::class.java).apply {
						putExtra("id", item.id)
						putExtra("isMovie", isMovie)
					}
					activity.startActivity(i)
				}
			}
		}
	}
}