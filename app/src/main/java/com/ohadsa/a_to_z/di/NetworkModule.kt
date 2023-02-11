package com.ohadsa.a_to_z.di

import com.ohadsa.a_to_z.network.GenreApi
import com.ohadsa.a_to_z.network.MovieApi
import com.ohadsa.a_to_z.network.PersonApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.ohadsa.a_to_z.network.TVShowApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideGsonFactory(): Converter.Factory = GsonConverterFactory.create()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // full log please
    }

    @Provides
    fun provideAuthorizationInterceptor() = Interceptor { chain ->
        val originalRequest = chain.request()
        val url =
            originalRequest.url.newBuilder().addQueryParameter("api_key", TMDB_API_KEY)
                .build()
        val newRequest = originalRequest.newBuilder().url(url).build()
        chain.proceed(newRequest)
    }

    @Singleton
    @Provides
    fun provideOKHTTPClient(
        authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor).build()


    @Singleton
    @Provides
    fun provideRetrofit(
        httpClient: OkHttpClient,
        gsonConverterFactory: Converter.Factory,
    ): Retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(TMDB_BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .build()



    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieApi = retrofit.create(MovieApi::class.java)
    @Provides
    fun provideTVService(retrofit: Retrofit): TVShowApi =retrofit.create(TVShowApi::class.java)
    @Provides
    fun providePersonService(retrofit: Retrofit): PersonApi = retrofit.create(PersonApi::class.java)
    @Provides
    fun provideGenreService(retrofit: Retrofit): GenreApi = retrofit.create(GenreApi::class.java)
}

//todo - should go to build config
const val TMDB_API_KEY = "b3b1492d3e91e9f9403a2989f3031b0c"
const val TMDB_BASE_URL = "https://api.themoviedb.org/"