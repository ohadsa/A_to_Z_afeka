package com.ohadsa.a_to_z.network

import com.ohadsa.a_to_z.models.*


interface RemoteApi  {
    //Movies:
    suspend fun popularMovies(page: Int = 1): Result<ItemWrapper<Movie>>
    suspend fun topRatedMovies(page: Int = 1): Result<ItemWrapper<Movie>>
    suspend fun latestMovies(page: Int = 1): Result<ItemWrapper<Movie>>
    suspend fun upComingMovies(page: Int = 1): Result<ItemWrapper<Movie>>

    suspend fun searchMovies(page: Int = 1, query: String): Result<ItemWrapper<Movie>>
    suspend fun movieTrailers(movieId: Int): Result<VideoWrapper>
    suspend fun movieCredits(movieId: Int): Result<CreditWrapper>
    suspend fun movieById(movieId: Int):Movie?

    //TV:
    suspend fun popularTVShows(page: Int = 1): Result<ItemWrapper<TVShow>>
    suspend fun topRatedTVShows(page: Int = 1): Result<ItemWrapper<TVShow>>
    suspend fun latestTVShows(page: Int = 1): Result<ItemWrapper<TVShow>>
    suspend fun searchTVShows(page: Int = 1, query: String): Result<ItemWrapper<TVShow>>
    suspend fun tvShowTrailers(tvId: Int): Result<VideoWrapper>
    suspend fun tvShowCredits(tvId: Int): Result<CreditWrapper>

    //Person:
    suspend fun getPerson(personId: Int): Result<Person>

    //Genre
    suspend fun genres(): Result<GenreWrapper>
    suspend fun  genreMovies(genreId : Int) : Result<ItemListWrapper<Movie>>
}


