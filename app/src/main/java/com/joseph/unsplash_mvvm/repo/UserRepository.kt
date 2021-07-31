package com.joseph.unsplash_mvvm.repo

import android.util.Log
import com.joseph.unsplash_mvvm.data.remote.api.UserApi
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.models.User
import com.joseph.unsplash_mvvm.util.Resource
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi
) {
    suspend fun getUserProfile(username: String): Resource<User> {
        val response = try {
            userApi.getUserProfile(username)
        } catch (e: HttpException) {
            return Resource.Error("Http Connection Error")
        }

        return if (response.isSuccessful && response.body() != null) {
            Log.d("[TAG]", "success")
            Resource.Success(response.body()!!)
        } else {
            Resource.Error("Error")
        }
    }

    suspend fun getUsersPhotos(username: String, page: Int): Resource<List<Photo>> {
        val response = try {
            userApi.getUsersPhotos(username, page)
        } catch (e: HttpException) {
            return Resource.Error("Http Connection Error")
        }

        return if (response.isSuccessful && response.body() != null) {
            Resource.Success(response.body() ?: throw RuntimeException("데이터가 존재하지 않습니다"))
        } else {
            Timber.d("TAG" + "[ERROR]")
            Resource.Error(response.message())
        }
    }
}