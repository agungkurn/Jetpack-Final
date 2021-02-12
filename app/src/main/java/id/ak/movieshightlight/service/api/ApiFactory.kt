package id.ak.movieshightlight.service.api

import com.google.gson.GsonBuilder
import id.ak.movieshightlight.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
	private val client = OkHttpClient.Builder()
		.addInterceptor(
			HttpLoggingInterceptor().apply {
				level = HttpLoggingInterceptor.Level.BODY
			}
		).build()

	val apiService: ApiService = Retrofit.Builder()
		.addConverterFactory(
			GsonConverterFactory.create(
				GsonBuilder().serializeNulls().create()
			)
		).baseUrl(BuildConfig.BASE_URL)
		.client(client)
		.build()
		.create(ApiService::class.java)
}