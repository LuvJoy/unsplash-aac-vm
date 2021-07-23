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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val userRepository: UserRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class Event {
        data class ErrorEvent(val message: String) : Event()
    }

    private val _randomPhoto: MutableStateFlow<Photo?> = MutableStateFlow(null)
    val randomPhoto get() = _randomPhoto

    private val _userProfile: MutableStateFlow<User?> = MutableStateFlow(null)
    val userProfile get() = _userProfile

    private val _errorEvent: MutableSharedFlow<Event> = MutableSharedFlow()
    val errorEvent get() = _errorEvent

    init {
        getRandomPhoto()
    }

    private fun getRandomPhoto() {
        viewModelScope.launch(dispatchers.main) {
            getPhoto()
            getUserProfile(_randomPhoto.value?.user?.username!!)
        }
    }

    private suspend fun getPhoto() = withContext(dispatchers.io) {
        val photo = photoRepository.getRandomPhoto()
        when (photo) {
            is Resource.Error -> {
                _errorEvent.emit(Event.ErrorEvent(photo.message!!))
            }
            is Resource.Success -> {
                _randomPhoto.value = photo.data
            }
        }
    }

    private suspend fun getUserProfile(username: String) = withContext(dispatchers.io) {
        val user = userRepository.getUserProfile(username)
        when (user) {
            is Resource.Error -> {
                _errorEvent.emit(Event.ErrorEvent(user.message!!))
                Timber.d(user.message)
            }
            is Resource.Success -> {
                _userProfile.value = user.data
            }
        }
    }
}