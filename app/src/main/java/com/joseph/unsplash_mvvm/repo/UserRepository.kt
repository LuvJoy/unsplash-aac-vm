package com.joseph.unsplash_mvvm.repo

import android.util.Log
import com.joseph.unsplash_mvvm.data.remote.api.UserApi
import com.joseph.unsplash_mvvm.models.User
import com.joseph.unsplash_mvvm.util.Resource
import retrofit2.HttpException
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
}