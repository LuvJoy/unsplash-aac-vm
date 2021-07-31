package com.joseph.unsplash_mvvm.data.remote.api

import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.models.User
import com.joseph.unsplash_mvvm.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("users/{username}")
    suspend fun getUserProfile(
        @Path("username") username: String
    ): Response<User>

    @GET("users/{username}/photos")
    suspend fun getUsersPhotos(
        @Path("username") username: String,
        @Query("page") page: Int = 1,
        @Query("per_page") per_page: Int = 10
    ): Response<List<Photo>>
}