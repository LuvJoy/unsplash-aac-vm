package com.joseph.unsplash_mvvm.data.remote.api

import com.joseph.unsplash_mvvm.models.Photo
import retrofit2.Response
import retrofit2.http.GET

interface PhotoApi {
    @GET("photos/random")
    suspend fun getRandomPhoto(): Response<Photo>
}