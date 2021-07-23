package com.joseph.unsplash_mvvm.data.remote.api

import com.joseph.unsplash_mvvm.models.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("users/{username}")
    suspend fun getUserProfile(
        @Path("username") username: String
    ): Response<User>
}