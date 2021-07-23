package com.joseph.unsplash_mvvm.di

import com.joseph.unsplash_mvvm.repo.PhotoRepository
import com.joseph.unsplash_mvvm.data.remote.api.PhotoApi
import com.joseph.unsplash_mvvm.util.Constants.API_KEY
import com.joseph.unsplash_mvvm.util.Constants.BASE_URL
import com.joseph.unsplash_mvvm.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                it.proceed(original)
            }
            .addNetworkInterceptor {
                val builder = it.request().newBuilder()
                builder.addHeader("Authorization", API_KEY)
                it.proceed(builder.build())
            }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePhotoApi(retrofit: Retrofit): PhotoApi {
        return retrofit.create(PhotoApi::class.java)
    }

    @Provides
    @Singleton
    fun providePhotoRepository(photoApi: PhotoApi): PhotoRepository {
        return PhotoRepository(photoApi)
    }

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider {
        return object : DispatcherProvider {
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val default: CoroutineDispatcher
                get() = Dispatchers.Default
        }
    }
}