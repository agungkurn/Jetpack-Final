package id.ak.movieshightlight.domain

import java.lang.Exception

sealed class ResultStatus<out T> {
	object Loading : ResultStatus<Nothing>()
	data class Success<out T>(val data: T) : ResultStatus<T>()
	data class Failed(val message: String? = null, val exception: Exception? = null) :
		ResultStatus<Nothing>()
}
