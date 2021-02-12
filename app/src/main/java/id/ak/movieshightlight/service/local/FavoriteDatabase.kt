package id.ak.movieshightlight.service.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.ak.movieshightlight.data.local.FavoriteItem

@Database(entities = [FavoriteItem::class], version = 1)
abstract class FavoriteDatabase : RoomDatabase() {
	companion object {
		@Volatile
		private var INSTANCE: FavoriteDatabase? = null

		fun getInstance(context: Context): FavoriteDatabase =
			INSTANCE ?: synchronized(this) {
				INSTANCE ?: Room.databaseBuilder(
					context.applicationContext, FavoriteDatabase::class.java, "db_favorite"
				).build()
			}
	}

	abstract fun favoriteDao(): FavoriteDao
}