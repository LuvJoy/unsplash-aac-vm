package com.joseph.unsplash_mvvm.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.models.PhotoSearchResult
import com.joseph.unsplash_mvvm.models.User
import com.joseph.unsplash_mvvm.repo.PhotoRepository
import com.joseph.unsplash_mvvm.repo.UserRepository
import com.joseph.unsplash_mvvm.util.DispatcherProvider
import com.joseph.unsplash_mvvm.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val userRepository: UserRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _randomPhoto: MutableStateFlow<Resource<Photo>> = MutableStateFlow(Resource.Loading())
    val randomPhoto: StateFlow<Resource<Photo>> get() = _randomPhoto

    private val _userProfile: MutableStateFlow<Resource<User>> = MutableStateFlow(Resource.Loading())
    val userProfile: StateFlow<Resource<User>> get() = _userProfile

    private val _naturePhotos: MutableStateFlow<Resource<PhotoSearchResult>> = MutableStateFlow(Resource.Loading())
    val naturePhotos: StateFlow<Resource<PhotoSearchResult>> get() = _naturePhotos

    private val _animalPhotos: MutableStateFlow<Resource<PhotoSearchResult>> = MutableStateFlow(Resource.Loading())
    val animalPhotos: StateFlow<Resource<PhotoSearchResult>> get() = _animalPhotos

    private var naturePage = 0
    private var animalPage = 0

    init {
        getRandomPhotoAndUserProfile()
        getNaturePhotos()
        getAnimalPhotos()
    }

    private fun getRandomPhotoAndUserProfile() {
        viewModelScope.launch(dispatchers.main) {
            _randomPhoto.value = Resource.Loading()
            Log.d("[TAG]", "main -> " + currentCoroutineContext().toString())
            _randomPhoto.value = getRandomPhoto()

            val state = _randomPhoto.value
            _userProfile.value = when (state) {
                is Resource.Success -> {
                    getUserProfile(state.data?.user?.username!!)
                }
                is Resource.Error -> {
                    Resource.Error(message = state.message ?: "")
                }
                is Resource.Loading -> {
                    Resource.Loading()
                }
            }
        }
    }

    private suspend fun getRandomPhoto() = withContext(dispatchers.io) {
        _randomPhoto.value = Resource.Loading()
        photoRepository.getRandomPhoto()
    }

    private suspend fun getUserProfile(username: String) = withContext(dispatchers.io) {
        _userProfile.value = Resource.Loading()
        userRepository.getUserProfile(username)
    }

    fun getNaturePhotos() = viewModelScope.launch {
        naturePage++
        _naturePhotos.value = Resource.Loading()
        _naturePhotos.value = photoRepository.searchPhotos("nature", naturePage)
    }

    fun getAnimalPhotos() = viewModelScope.launch {
        animalPage++
        _animalPhotos.value = Resource.Loading()
        _animalPhotos.value = photoRepository.searchPhotos("animal", animalPage)
    }
}