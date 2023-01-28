package com.ohadsa.a_to_z.network


import CommandRequest
import com.ohadsa.a_to_z.models.MovieResponse
import com.ohadsa.a_to_z.models.MyUser
import com.ohadsa.a_to_z.models.UserResponse
import retrofit2.http.*


interface MoviesApi {

    @GET("/superapp/users/{superapp}/{email}")
    suspend fun login(
        @Path("superapp") superapp: String = "2023.ohad.saada",
        @Path("email") email: String,
    ): UserResponse?


    @POST("/superapp/users")
    suspend fun signup(@Body value: MyUser): UserResponse

    @PUT("/superapp/users/{superapp}/{userEmail}")
    suspend fun updateUser(
        @Path("superapp") superapp: String = SuperApp,
        @Path("userEmail") userEmail: String,
        @Body value: UserResponse,
    ): UserResponse?

//{"userSuperapp", "userEmail", "size", "page"}@Query("address") String address,


    @GET("/superapp/objects/searchbyAlias/{alias}")
    suspend fun getUpcomingMovies(
        @Path("alias") alias: String = upcoming,
        @Query("userSuperapp") userSuperapp: String = SuperApp,
        @Query("userEmail") userEmail: String = "test@example.com",
        @Query("size") size: Int = 10,
        @Query("page") page: Int? = 0,

        ): List<MovieResponse>?

    @GET("/superapp/objects/searchbyAlias/{alias}")
    suspend fun getNowPlayingMovies(
        @Path("alias") alias: String = nowPlaying,
        @Query("userSuperapp") userSuperapp: String = SuperApp,
        @Query("userEmail") userEmail: String = "test@example.com",
        @Query("size") size: Int = 10,
        @Query("page") page: Int? = 0,

        ): List<MovieResponse>?

    @GET("/superapp/objects/searchbyAlias/{alias}")
    suspend fun getTopRatedMovies(
        @Path("alias") alias: String = topRated,
        @Query("userSuperapp") userSuperapp: String = SuperApp,
        @Query("userEmail") userEmail: String = "test@example.com",
        @Query("size") size: Int = 10,
        @Query("page") page: Int? = 0,

        ): List<MovieResponse>?


    @POST("/superapp/miniapp/{miniAppName}")
    suspend fun addToFavorite(
        @Path("miniAppName") miniAppName: String = addToFavorite,
        @Body value: CommandRequest,
    )

    @POST("/superapp/miniapp/{miniAppName}")
    suspend fun removeFromFavorite(
        @Path("miniAppName") miniAppName: String = removeFromFavorite,
        @Body value: CommandRequest,
    )

    @POST("/superapp/miniapp/{miniAppName}")
    suspend fun getAllFavorite(
        @Path("miniAppName") miniAppName: String = getAllFavorite,
        @Body value: CommandRequest,
    ): List<MovieResponse>?


    @POST("/superapp/miniapp/{miniAppName}")
    suspend fun addToWish(
        @Path("miniAppName") miniAppName: String = addToWishlist,
        @Body value: CommandRequest,
    )

    @POST("/superapp/miniapp/{miniAppName}")
    suspend fun removeFromWish(
        @Path("miniAppName") miniAppName: String = removeFromWishlist,
        @Body value: CommandRequest,
    )

    @POST("/superapp/miniapp/{miniAppName}")
    suspend fun getAllWish(
        @Path("miniAppName") miniAppName: String = getAllWishlist,
        @Body value: CommandRequest,
    ): List<MovieResponse>?



}


const val SuperApp = "2023.ohad.saada"
const val upcoming = "upcoming"
const val nowPlaying = "nowPlaying"

const val addToFavorite = "addToFavorite"
const val removeFromFavorite = "removeFromFavorite"
const val getAllFavorite = "getAllFavorite"

const val addToWishlist = "addToWishlist"
const val removeFromWishlist = "removeFromWishlist"
const val getAllWishlist = "getAllWishlist"

const val topRated = "topRated"
const val movie = "upcoming"


/*************************
 * superapp/miniapp/{miniAppName} = AddToWatchList
{
"targetObject": {
"objectId": {
"superApp": "2023.ohad.saada",
"internalObjectId": "1"
}
},
"invokedBy": {
"userId": {
"superapp": "2023.ohad.saada",
"email": "test@example.com"
}
}
}

kotlin Command object :
CommandRequest(
targetObject =
TargetObject(
objectId = ObjectId(
superApp = "2023.ohad.saada",
internalObjectId = "12")
),
invokedBy =
InvokedBy(
userId = UserId(
superapp = "2023.ohad.saada",
email = "ohad@ohad.com"
)
)
),
 **************************/
