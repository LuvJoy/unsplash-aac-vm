package com.joseph.unsplash_mvvm.ui.user_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.models.User
import com.joseph.unsplash_mvvm.repo.UserRepository
import com.joseph.unsplash_mvvm.util.DispatcherProvider
import com.joseph.unsplash_mvvm.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    sealed class UserDetailEvent() {
        object Loading : UserDetailEvent()
        class LoadUsersPhotoSuccess(val data: List<Photo>) : UserDetailEvent()
        class LoadUsersPhotoFailed(val message: String) : UserDetailEvent()
    }

    private var _usersPhotos = MutableStateFlow<UserDetailEvent>(UserDetailEvent.Loading)
    val usersPhotos: StateFlow<UserDetailEvent> get() = _usersPhotos

    private var _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private var usersPhotosPage = 0

    init {
        _user.value =
            savedStateHandle.get<User>("user") ?: throw RuntimeException("user data is Null")
        getUsersPhotos()
    }

    fun getUsersPhotos() = viewModelScope.launch {
        _usersPhotos.value = UserDetailEvent.Loading
        usersPhotosPage++
        val username = _user.value?.username
        val response = userRepository.getUsersPhotos(username ?: return@launch, usersPhotosPage)

        when (response) {
            is Resource.Success -> {
                _usersPhotos.value = UserDetailEvent.LoadUsersPhotoSuccess(response.data ?: return@launch)
            }

            is Resource.Error -> {
                Timber.d("TAG + ${response.message}")
                _usersPhotos.value = UserDetailEvent.LoadUsersPhotoFailed(response.message ?: return@launch)
            }
        }
    }

}