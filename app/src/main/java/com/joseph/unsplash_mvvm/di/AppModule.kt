package com.joseph.unsplash_mvvm.di

import com.joseph.unsplash_mvvm.data.remote.api.PhotoApi
import com.joseph.unsplash_mvvm.data.remote.api.UserApi
import com.joseph.unsplash_mvvm.repo.PhotoRepository
import com.joseph.unsplash_mvvm.repo.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserRepository(photoApi: UserApi): UserRepository {
        return UserRepository(photoApi)
    }

    @Provides
    @Singleton
    fun providePhotoRepository(photoApi: PhotoApi): PhotoRepository {
        return PhotoRepository(photoApi)
    }
}