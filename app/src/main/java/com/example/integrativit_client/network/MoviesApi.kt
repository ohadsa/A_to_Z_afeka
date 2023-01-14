package com.example.integrativit_client.network


import com.example.integrativit_client.models.MyUser
import com.example.integrativit_client.models.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface MoviesAPi {

    @GET("/superapp/users/2023.ohad.saada/{email}")
    suspend fun login(@Path("email") email: String ): UserResponse


    @POST("/superapp/users")
    suspend fun signup(@Body value: MyUser): UserResponse
//    @GET("/3/discover/movie&with_genres={genreId}")
//    suspend fun genreMovies(@Path("genreId") genreId: Int ) : ItemListWrapper<Movie>



}

const val SuperApp = "2023.ohad.saada"




/***************************
    end points:
    https://api.spacexdata.com/v4/ships
    https://api.spacexdata.com/v5/launches
 **************************/
