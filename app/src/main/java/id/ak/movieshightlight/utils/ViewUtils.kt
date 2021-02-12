package id.ak.movieshightlight.utils

import android.view.View
import android.view.View.*

fun View.visible() {
	visibility = VISIBLE
}

fun View.invisible() {
	visibility = INVISIBLE
}

fun View.gone() {
	visibility = GONE
}

fun View.enable() {
	isEnabled = true
}

fun View.disable() {
	isEnabled = false
}