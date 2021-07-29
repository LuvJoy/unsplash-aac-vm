package com.joseph.unsplash_mvvm.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.models.User
import com.joseph.unsplash_mvvm.repo.PhotoRepository
import com.joseph.unsplash_mvvm.repo.UserRepository
import com.joseph.unsplash_mvvm.util.DispatcherProvider
import com.joseph.unsplash_mvvm.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val userRepository: UserRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class Event {
        data class LoadRandomPhotoEvent(val data: Photo) : Event()
        object LoadRandomPhotoLoadingEvent : Event()
        data class LoadRandomPhotoErrorEvent(val message: String) : Event()

        data class LoadUserProfileEvent(val userProfile: User) : Event()
        data class LoadUserProfileErrorEvent(val message: String) : Event()

        data class SearchPhotoEvent(val photos: List<Photo>) : Event()
        data class SearchPhotoErrorEvent(val message: String) : Event()
    }

    private val _randomPhoto: MutableStateFlow<Event?> = MutableStateFlow(null)
    val randomPhoto get() = _randomPhoto

    private val _userProfile: MutableStateFlow<Event?> = MutableStateFlow(null)
    val userProfile get() = _userProfile

    private val _naturePhotos: MutableStateFlow<Event?> = MutableStateFlow(null)
    val naturePhotos get() = _naturePhotos

    private val _animalPhotos: MutableStateFlow<Event?> = MutableStateFlow(null)
    val animalPhotos get() = _animalPhotos

    private val _errorEvent: MutableSharedFlow<Event> = MutableSharedFlow()
    val errorEvent get() = _errorEvent

    private var naturePage = 0
    private var animalPage = 0

    init {
        getRandomPhotoAndUserProfile()
        getNaturePhotos()
        getAnimalPhotos()
    }

    private fun getRandomPhotoAndUserProfile() {
        viewModelScope.launch(dispatchers.main) {
            _randomPhoto.value = Event.LoadRandomPhotoLoadingEvent
            Log.d("[TAG]", "main -> " + currentCoroutineContext().toString())
            _randomPhoto.value = getRandomPhoto()

            val event = _randomPhoto.value
            _userProfile.value = when (event) {
                is Event.LoadRandomPhotoEvent -> {
                    getUserProfile(event.data.user?.username!!)
                }
                is Event.LoadRandomPhotoErrorEvent -> {
                    Event.LoadRandomPhotoErrorEvent(event.message)
                }
                else -> return@launch
            }
        }
    }

    private suspend fun getRandomPhoto() = withContext(dispatchers.io) {
        Log.d("[TAG]", "photo() io -> " + currentCoroutineContext().toString())
        val response = photoRepository.getRandomPhoto()
        when (response) {
            is Resource.Error -> {
                Event.LoadRandomPhotoErrorEvent(response.message!!)
            }
            is Resource.Success -> {
                Event.LoadRandomPhotoEvent(response.data!!)
            }
        }
    }

    private suspend fun getUserProfile(username: String) = withContext(dispatchers.io) {
        Log.d("[TAG]", "user() io -> " + currentCoroutineContext().toString())
        val response = userRepository.getUserProfile(username)
        when (response) {
            is Resource.Error -> {
                Event.LoadUserProfileErrorEvent(response.message!!)
            }
            is Resource.Success -> {
                Event.LoadUserProfileEvent(response.data!!)
            }
        }
    }

    fun getNaturePhotos() = viewModelScope.launch {
        naturePage++
        val response = photoRepository.searchPhotos("nature", naturePage)
        when (response) {
            is Resource.Error -> {
                _naturePhotos.value =
                    Event.SearchPhotoErrorEvent(response.message ?: return@launch)
            }
            is Resource.Success -> {
                _naturePhotos.value =
                    Event.SearchPhotoEvent(response.data?.results ?: return@launch)
            }
        }
    }

    fun getAnimalPhotos() = viewModelScope.launch {
        animalPage++
        val response = photoRepository.searchPhotos("animal", animalPage)
        when (response) {
            is Resource.Error -> {
                _animalPhotos.value =
                    Event.SearchPhotoErrorEvent(response.message ?: return@launch)
            }
            is Resource.Success -> {
                _animalPhotos.value =
                    Event.SearchPhotoEvent(response.data?.results ?: return@launch)
            }
        }
    }


}