package com.joseph.unsplash_mvvm.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.repo.PhotoRepository
import com.joseph.unsplash_mvvm.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) :ViewModel() {

    sealed class SearchEvent() {
        class SearchPhotosSuccess(data: List<Photo>) : SearchEvent()
        class SearchPhotosFailed(message: String) : SearchEvent()
        object SearchPhotosQueryEmpty : SearchEvent()
        object Loading : SearchEvent()
    }

    private val _photos = MutableStateFlow<SearchEvent>(SearchEvent.SearchPhotosQueryEmpty)
    val photos get() = _photos

    private var lastQuery = ""
    private var page = 1

    fun searchPhotos(query: String) {
    }
}

