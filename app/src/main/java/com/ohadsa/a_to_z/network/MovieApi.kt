package com.ohadsa.a_to_z.network

import com.ohadsa.a_to_z.models.CreditWrapper
import com.ohadsa.a_to_z.models.ItemWrapper
import com.ohadsa.a_to_z.models.Movie
import com.ohadsa.a_to_z.models.VideoWrapper
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("3/discover/movie?language=en&sort_by=popularity.desc")
    suspend fun popularItems(@Query("page") page: Int = 1): ItemWrapper<Movie>

    @GET("3/movie/top_rated?language=en")
    suspend fun topRatedItems(@Query("page") page: Int = 1): ItemWrapper<Movie>

    @GET("3/movie/upcoming?language=en")
    suspend fun latestItems(@Query("page") page: Int = 1): ItemWrapper<Movie>

    @GET("3/movie/now_playing?language=en")
    suspend fun upcomingItems(@Query("page") page: Int = 1): ItemWrapper<Movie>


    @GET("3/search/movie?language=en")
    suspend fun searchItems(
        @Query("page") page: Int = 1,
        @Query("query") query: String,
    ): ItemWrapper<Movie>

    @GET("3/movie/{movieId}/videos")
    suspend fun movieTrailers(@Path("movieId") movieId: Int = 1): VideoWrapper

    @GET("3/movie/{movieId}/credits")
    suspend fun movieCredit(@Path("movieId") movieId: Int = 1): CreditWrapper

    @GET("3/movie/{movieId}")
    suspend fun movieById(@Path("movieId") movieId: Int): Movie?


}