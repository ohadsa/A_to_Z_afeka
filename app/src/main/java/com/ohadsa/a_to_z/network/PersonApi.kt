package com.ohadsa.a_to_z.network

import com.ohadsa.a_to_z.models.Person
import retrofit2.http.GET
import retrofit2.http.Path

interface PersonApi {
    @GET("3/person/{personId}")
    suspend fun getPerson(@Path("personId") personId: Any): Person
}