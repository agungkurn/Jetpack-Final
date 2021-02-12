package id.ak.movieshightlight.presentation.discover

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import id.ak.movieshightlight.R
import id.ak.movieshightlight.utils.EspressoIdlingResource
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MainActivityTest {
	@Before
	fun setup() {
		ActivityScenario.launch(MainActivity::class.java)
		IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource())
	}

	@Test
	fun addMovieToFavorite() {
		onView(withId(R.id.navigation_movie))
			.check(matches(isDisplayed()))
			.perform(click())

		for (i in 0..7) {
			onView(withId(R.id.rv_movies))
				.check(matches(isDisplayed()))
				.perform(
					scrollToPosition<RecyclerView.ViewHolder>(i),
					actionOnItemAtPosition<RecyclerView.ViewHolder>(i, click())
				)

			onView(withId(R.id.iv_banner))
				.check(matches(isDisplayed()))
			onView(withId(R.id.iv_poster))
				.check(matches(isDisplayed()))
			onView(withId(R.id.tv_title))
				.check(matches(isDisplayed()))
			onView(withId(R.id.tv_year))
				.check(matches(isDisplayed()))
				.check(matches(withSubstring("20")))
			onView(withId(R.id.scroll_detail))
				.perform(swipeUp())
			onView(withId(R.id.tv_overview))
				.check(matches(isDisplayed()))
			onView(withId(R.id.rating))
				.check(matches(isDisplayed()))
			onView(withId(R.id.tv_language))
				.check(matches(isDisplayed()))
			onView(withId(R.id.tv_runtime))
				.check(matches(isDisplayed()))
			onView(withId(R.id.btn_favorite))
				.check(matches(isDisplayed()))
				.perform(click())
			onView(
				allOf(
					withId(com.google.android.material.R.id.snackbar_text),
					withText(R.string.success_add_favorite)
				)
			).check(matches(isDisplayed()))

			pressBack()
		}
	}

	@Test
	fun addTvShowToFavorite() {
		onView(withId(R.id.navigation_tv))
			.check(matches(isDisplayed()))
			.perform(click())

		for (i in 0..7) {
			onView(withId(R.id.rv_tv))
				.check(matches(isDisplayed()))
				.perform(
					scrollToPosition<RecyclerView.ViewHolder>(i),
					actionOnItemAtPosition<RecyclerView.ViewHolder>(i, click())
				)

			onView(withId(R.id.iv_banner))
				.check(matches(isDisplayed()))
			onView(withId(R.id.iv_poster))
				.check(matches(isDisplayed()))
			onView(withId(R.id.tv_title))
				.check(matches(isDisplayed()))
			onView(withId(R.id.tv_year))
				.check(matches(isDisplayed()))
				.check(matches(withSubstring("20")))
			onView(withId(R.id.scroll_detail))
				.perform(swipeUp())
			onView(withId(R.id.tv_overview))
				.check(matches(isDisplayed()))
			onView(withId(R.id.rating))
				.check(matches(isDisplayed()))
			onView(withId(R.id.tv_language))
				.check(matches(isDisplayed()))
			onView(withId(R.id.tv_runtime))
				.check(matches(isDisplayed()))
			onView(withId(R.id.btn_favorite))
				.check(matches(isDisplayed()))
				.perform(click())
			onView(
				allOf(
					withId(com.google.android.material.R.id.snackbar_text),
					withText(R.string.success_add_favorite)
				)
			).check(matches(isDisplayed()))

			pressBack()
		}
	}

	@Test
	fun loadFavorites() {
		onView(withId(R.id.navigation_favorite))
			.check(matches(isDisplayed()))
			.perform(click())

		// Select a favorite movie, see details, remove from favorite
		onView(withId(R.id.rv_movies))
			.check(matches(isDisplayed()))
			.perform(
				actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
			)
		onView(withId(R.id.scroll_detail))
			.perform(swipeUp())
		onView(withId(R.id.btn_favorite))
			.check(matches(isDisplayed()))
			.perform(click())
		onView(
			allOf(
				withId(com.google.android.material.R.id.snackbar_text),
				withText(R.string.success_remove_favorite)
			)
		).check(matches(isDisplayed()))
		pressBack()

		// Select a favorite TV show, see details, remove from favorite
		onView(withId(R.id.scroll_favorite))
			.perform(swipeUp())
		onView(withId(R.id.rv_tv))
			.check(matches(isDisplayed()))
			.perform(
				actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
			)
		onView(withId(R.id.scroll_detail))
			.perform(swipeUp())
		onView(withId(R.id.btn_favorite))
			.check(matches(isDisplayed()))
			.perform(click())
		onView(
			allOf(
				withId(com.google.android.material.R.id.snackbar_text),
				withText(R.string.success_remove_favorite)
			)
		).check(matches(isDisplayed()))
		pressBack()
	}

	@After
	fun tearDown() {
		IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource())
	}
}