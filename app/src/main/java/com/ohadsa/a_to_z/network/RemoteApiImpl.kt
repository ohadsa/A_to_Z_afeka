package com.ohadsa.a_to_z.network

import android.util.Log
import com.ohadsa.a_to_z.models.*
import java.io.IOException
import javax.inject.Inject

class RemoteApiImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val TVApi: TVShowApi,
    private val personApi: PersonApi,
    private val genreApi: GenreApi

) : RemoteApi {

    // movies implementation Api
    override suspend fun popularMovies(page: Int): Result<ItemWrapper<Movie>> =
        try {
            Success(movieApi.popularItems(page))
        } catch (e: Throwable) {
            Failure(e)
        }


    override suspend fun topRatedMovies(page: Int): Result<ItemWrapper<Movie>> =
        try {
            Success(movieApi.topRatedItems(page))
        } catch (e: Throwable) {
            Failure(e)
        }

    override suspend fun latestMovies(page: Int): Result<ItemWrapper<Movie>> =
        try {
            Success(movieApi.latestItems(page))
        } catch (e: Throwable) {
            Failure(e)
        }

    override suspend fun upComingMovies(page: Int): Result<ItemWrapper<Movie>> =
        try {
            Success(movieApi.upcomingItems(page))
        } catch (e: Throwable) {
            Failure(e)
        }

    override suspend fun searchMovies(page: Int, query: String): Result<ItemWrapper<Movie>> =
        try {
            Success(movieApi.searchItems(page, query))
        } catch (e: Throwable) {
            Failure(e)
        }

    override suspend fun movieTrailers(movieId: Int): Result<VideoWrapper> =
        try {
            Success(movieApi.movieTrailers(movieId))
        } catch (e: Throwable) {
            Failure(e)
        }

    override suspend fun movieCredits(movieId: Int): Result<CreditWrapper> =
        try {
            Success(movieApi.movieCredit(movieId))
        } catch (e: Throwable) {
            Failure(e)
        }

    override suspend fun movieById(movieId: Int): Movie? =
        (movieApi.movieById(movieId))



    // tvShow implementation Api
    override suspend fun popularTVShows(page: Int): Result<ItemWrapper<TVShow>> =
        try {
            Success(TVApi.popularItems(page))
        } catch (e: Throwable) {
            Failure(e)
        }


    override suspend fun topRatedTVShows(page: Int): Result<ItemWrapper<TVShow>> =
        try {
            Success(TVApi.topRatedItems(page))
        } catch (e: Throwable) {
            Failure(e)
        }

    override suspend fun latestTVShows(page: Int): Result<ItemWrapper<TVShow>> =
        try {
            Success(TVApi.latestItems(page))
        } catch (e: Throwable) {
            Failure(e)
        }

    override suspend fun searchTVShows(page: Int, query: String): Result<ItemWrapper<TVShow>> =
        try {
            Success(TVApi.searchItems(page, query))
        } catch (e: Throwable) {
            Failure(e)
        }

    override suspend fun tvShowTrailers(tvId: Int): Result<VideoWrapper> =
        try {
            Success(TVApi.tvTrailers(tvId))
        } catch (e: Throwable) {
            Failure(e)
        }

    override suspend fun tvShowCredits(tvId: Int): Result<CreditWrapper> =
        try {
            Success(TVApi.tvCredit(tvId))
        } catch (e: Throwable) {
            Failure(e)
        }


    // person implementation Api
    override suspend fun getPerson(personId: Int): Result<Person> =
        try {
            Success(personApi.getPerson(personId))
        } catch (e: Throwable) {
            Failure(e)
        }

    override suspend fun genres(): Result<GenreWrapper> =
        try {
            Success(genreApi.genres())
        } catch (e: Throwable) {
            Failure(e)
        }

    override suspend fun genreMovies(genreId: Int) =
        try {
            Success(genreApi.genreMovies(genreId))
        } catch (e: Throwable) {
            Failure(e)
        }

}

private const val TAG = "RemoteApiImpl"
private suspend fun <T : Any> performCatching(block: suspend () -> T) =
    try {
        Success(block())
    } catch (t: Throwable) {
        // Todo: Change to logger
        Log.e(TAG, t.message.toString())

        if (t is IOException)
            Failure(TmdbThrowable.TmdbNetworkThrowable)
        else
            Failure(TmdbThrowable.TmdbInternalThrowable)
    }


sealed class Result<out T:Any>

data class Success<out T:Any>(val data:T ): Result<T>(){
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}
data class Failure(val exc : Throwable? ): Result<Nothing>()

sealed class TmdbThrowable : Throwable() {
    object TmdbNetworkThrowable : TmdbThrowable()
    object TmdbServerThrowable : TmdbThrowable()
    object TmdbInternalThrowable : TmdbThrowable()
    object TmdbNotInitialized : TmdbThrowable()
    object TmdbBrokenThrowable : TmdbThrowable()
}