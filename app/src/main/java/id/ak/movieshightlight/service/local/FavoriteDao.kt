package id.ak.movieshightlight.service.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ak.movieshightlight.data.local.FavoriteItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
	@Query("SELECT * FROM favorite WHERE isMovie = 1")
	fun getFavoriteMovies(): DataSource.Factory<Int, FavoriteItem>

	@Query("SELECT * FROM favorite WHERE isMovie = 0")
	fun getFavoriteTvShows(): DataSource.Factory<Int, FavoriteItem>

	@Query("SELECT * FROM favorite WHERE id=:id")
	fun getDetails(id: Int): Flow<List<FavoriteItem>>

	@Query("SELECT count(*) FROM favorite WHERE id=:id")
	fun checkIfFavorite(id: Int): Flow<Int>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun addToFavorite(item: FavoriteItem)

	@Query("DELETE FROM favorite WHERE id=:id")
	suspend fun removeFromFavorite(id: Int)
}