package com.joseph.unsplash_mvvm.data.remote.api

import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.models.PhotoSearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApi {
    @GET("photos/random")
    suspend fun getRandomPhoto(): Response<Photo>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("order_by") order_by: String = "relevant",
        @Query("color") color: String?,
        @Query("orientation") orientation: String?
    ): Response<PhotoSearchResult>

}