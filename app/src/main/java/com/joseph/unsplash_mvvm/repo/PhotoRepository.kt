package com.joseph.unsplash_mvvm.repo

import com.joseph.unsplash_mvvm.data.remote.api.PhotoApi
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.util.Resource
import retrofit2.HttpException
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    val photoApi: PhotoApi
) {
    suspend fun getRandomPhoto(): Resource<Photo> {
        val response = try {
            photoApi.getRandomPhoto()
        } catch (e: HttpException) {
            return Resource.Error("Http ConnectionError")
        }

        return if(response.isSuccessful && response.body() != null) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error("response is failed")
        }
    }
}