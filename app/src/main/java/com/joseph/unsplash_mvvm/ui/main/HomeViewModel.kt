package com.joseph.unsplash_mvvm.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.unsplash_mvvm.models.photo.Photo
import com.joseph.unsplash_mvvm.repo.PhotoRepository
import com.joseph.unsplash_mvvm.util.DispatcherProvider
import com.joseph.unsplash_mvvm.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PhotoRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _randomPhoto: MutableStateFlow<Photo?> = MutableStateFlow(null)
    val randomPhoto get() = _randomPhoto

    init {
        getRandomPhoto()
    }

    private fun getRandomPhoto() {
        viewModelScope.launch(dispatchers.main) {
            val photo = repository.getRandomPhoto()
            when(photo) {
                is Resource.Error -> {

                }
                is Resource.Success -> {
                    _randomPhoto.value = photo.data
                }
            }
        }
    }
}