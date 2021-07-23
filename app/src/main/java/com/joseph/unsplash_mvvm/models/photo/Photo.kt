package com.joseph.unsplash_mvvm.models.photo


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Photo(
    @Json(name = "blur_hash")
    val blurHash: String?,
    @Json(name = "color")
    val color: String?,
    @Json(name = "created_at")
    val createdAt: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "downloads")
    val downloads: Int?,
    @Json(name = "exif")
    val exif: Exif?,
    @Json(name = "height")
    val height: Int?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "liked_by_user")
    val likedByUser: Boolean?,
    @Json(name = "likes")
    val likes: Int?,
    @Json(name = "links")
    val links: Links?,
    @Json(name = "location")
    val location: Location?,
    @Json(name = "updated_at")
    val updatedAt: String?,
    @Json(name = "urls")
    val urls: Urls?,
    @Json(name = "user")
    val user: User?,
    @Json(name = "width")
    val width: Int?
)