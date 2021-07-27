package com.joseph.unsplash_mvvm.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.models.User
import com.joseph.unsplash_mvvm.repo.UserRepository
import com.joseph.unsplash_mvvm.util.DispatcherProvider
import com.joseph.unsplash_mvvm.util.Resource
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: DispatcherProvider,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _photo = MutableStateFlow<Photo?>(null)
    val photo: StateFlow<Photo?> get() = _photo

    private val _userProfile = MutableSharedFlow<Event>()
    val userProfile: SharedFlow<Event> get() = _userProfile

    sealed class Event {
        class LoadUserProfileEvent(val data: User) : Event()
        class LoadUserProfileErrorEvent(val message: String) : Event()
    }

    init {
        _photo.value =
            savedStateHandle.get<Photo>("photo") ?: throw RuntimeException("Photo Data is Null")
        getUserProfile()
    }

    private fun getUserProfile() = viewModelScope.launch {
        val username = _photo.value?.user?.username
        Timber.d("[TAG] : $username")
        val response = userRepository.getUserProfile(
            username ?: throw RuntimeException("UserId cannot be null")
        )
        Timber.d("[TAG] : ${response.toString()}")
        when (response) {
            is Resource.Success -> {
                _userProfile.emit(
                    Event.LoadUserProfileEvent(
                        response.data ?: throw RuntimeException("Response User Data is Null")
                    )
                )
            }
            is Resource.Error -> {
                _userProfile.emit(
                    Event.LoadUserProfileErrorEvent(
                        response.message ?: throw RuntimeException("Response Message Data is Null")
                    )
                )
            }
        }
    }
}