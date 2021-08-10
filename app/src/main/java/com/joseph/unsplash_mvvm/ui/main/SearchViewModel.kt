package com.joseph.unsplash_mvvm.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.unsplash_mvvm.models.Photo
import com.joseph.unsplash_mvvm.models.PhotoSearchResult
import com.joseph.unsplash_mvvm.repo.PhotoRepository
import com.joseph.unsplash_mvvm.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) :ViewModel() {

    private var _photos = MutableStateFlow<Resource<PhotoSearchResult>?>(null)
    val photos: StateFlow<Resource<PhotoSearchResult>?> get() = _photos

    private var lastQuery = ""
    private var page = 1
    var isSameQuery = false

    fun searchPhotos(query: String) = viewModelScope.launch {
        _photos.value = Resource.Loading()
        if(lastQuery == query) {
            page++
            isSameQuery = true
            _photos.value = photoRepository.searchPhotos(query, page)
        } else {
            page = 1
            isSameQuery = false
            _photos.value = photoRepository.searchPhotos(query, page)
        }
    }
}

