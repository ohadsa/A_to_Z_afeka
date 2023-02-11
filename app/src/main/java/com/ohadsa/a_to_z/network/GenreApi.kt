package com.ohadsa.a_to_z.network

import com.ohadsa.a_to_z.models.GenreWrapper
import com.ohadsa.a_to_z.models.ItemListWrapper
import com.ohadsa.a_to_z.models.Movie
import retrofit2.http.GET
import retrofit2.http.Path


interface GenreApi {
    @GET("3/genre/movie/list?language=en-US")
    suspend fun genres(): GenreWrapper


    @GET("/3/discover/movie&with_genres={genreId}")
    suspend fun genreMovies(@Path("genreId") genreId: Int ) : ItemListWrapper<Movie>
}


