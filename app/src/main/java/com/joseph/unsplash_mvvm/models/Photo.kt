package com.joseph.unsplash_mvvm.models


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
) {
    @JsonClass(generateAdapter = true)
    data class Position(
        @Json(name = "latitude")
        val latitude: Double?,
        @Json(name = "longitude")
        val longitude: Double?
    )

    @JsonClass(generateAdapter = true)
    data class Urls(
        @Json(name = "full")
        val full: String?,
        @Json(name = "raw")
        val raw: String?,
        @Json(name = "regular")
        val regular: String?,
        @Json(name = "small")
        val small: String?,
        @Json(name = "thumb")
        val thumb: String?
    )

    @JsonClass(generateAdapter = true)
    data class User(
        @Json(name = "bio")
        val bio: String?,
        @Json(name = "id")
        val id: String?,
        @Json(name = "instagram_username")
        val instagramUsername: String?,
        @Json(name = "links")
        val userLinks: UserLinks?,
        @Json(name = "location")
        val location: String?,
        @Json(name = "name")
        val name: String?,
        @Json(name = "portfolio_url")
        val portfolioUrl: String?,
        @Json(name = "total_collections")
        val totalCollections: Int?,
        @Json(name = "total_likes")
        val totalLikes: Int?,
        @Json(name = "total_photos")
        val totalPhotos: Int?,
        @Json(name = "twitter_username")
        val twitterUsername: String?,
        @Json(name = "updated_at")
        val updatedAt: String?,
        @Json(name = "username")
        val username: String?
    )

    @JsonClass(generateAdapter = true)
    data class UserLinks(
        @Json(name = "html")
        val html: String?,
        @Json(name = "likes")
        val likes: String?,
        @Json(name = "photos")
        val photos: String?,
        @Json(name = "portfolio")
        val portfolio: String?,
        @Json(name = "self")
        val self: String?
    )

    @JsonClass(generateAdapter = true)
    data class Location(
        @Json(name = "city")
        val city: String?,
        @Json(name = "country")
        val country: String?,
        @Json(name = "name")
        val name: String?,
        @Json(name = "position")
        val position: Position?
    )

    @JsonClass(generateAdapter = true)
    data class Exif(
        @Json(name = "aperture")
        val aperture: String?,
        @Json(name = "exposure_time")
        val exposureTime: String?,
        @Json(name = "focal_length")
        val focalLength: String?,
        @Json(name = "iso")
        val iso: Int?,
        @Json(name = "make")
        val make: String?,
        @Json(name = "model")
        val model: String?
    )
}