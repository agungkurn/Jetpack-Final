package id.ak.movieshightlight.presentation.discover

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import id.ak.movieshightlight.R
import id.ak.movieshightlight.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private val binding by lazy {
		ActivityMainBinding.inflate(layoutInflater)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		setSupportActionBar(binding.toolbarMain)

		(supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment).also {
			setupActionBarWithNavController(
				it.navController,
				AppBarConfiguration(
					setOf(R.id.navigation_movie, R.id.navigation_tv, R.id.navigation_favorite)
				)
			)
			binding.navView.setupWithNavController(it.navController)
		}
	}
}