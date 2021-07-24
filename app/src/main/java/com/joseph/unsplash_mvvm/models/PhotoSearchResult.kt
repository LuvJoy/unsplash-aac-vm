package com.joseph.unsplash_mvvm.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoSearchResult(
    @Json(name = "total")
    val total: Int?,
    @Json(name = "total_pages")
    val total_pages: Int?,
    @Json(name = "results")
    val results: List<Photo>?
)
