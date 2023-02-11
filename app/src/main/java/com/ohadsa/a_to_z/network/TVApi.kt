package com.ohadsa.a_to_z.network


import com.ohadsa.a_to_z.models.CreditWrapper
import com.ohadsa.a_to_z.models.ItemWrapper
import com.ohadsa.a_to_z.models.TVShow
import com.ohadsa.a_to_z.models.VideoWrapper
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TVShowApi {

    @GET("3/tv/popular?language=en")
    suspend fun popularItems(@Query("page") page: Int = 1 ): ItemWrapper<TVShow>

    @GET("3/tv/top_rated?language=en")
    suspend fun topRatedItems(@Query("page") page: Int = 1 ): ItemWrapper<TVShow>

    @GET("3/tv/on_the_air?language=en")
    suspend fun latestItems(@Query("page") page: Int = 1 ): ItemWrapper<TVShow>

    @GET("3/search/tv?language=en")
    suspend fun searchItems(
        @Query("page") page: Int,
        @Query("query") query: String
    ): ItemWrapper<TVShow>

    @GET("3/tv/{tvId}/videos")
    suspend fun tvTrailers(@Path("tvId") tvId: Int): VideoWrapper

    @GET("3/tv/{tvId}/credits")
    suspend fun tvCredit(@Path("tvId") tvId: Int): CreditWrapper


}

