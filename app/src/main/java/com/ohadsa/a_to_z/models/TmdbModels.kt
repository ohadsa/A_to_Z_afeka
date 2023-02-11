package com.ohadsa.a_to_z.models

import com.google.gson.annotations.SerializedName

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class ItemWrapper<T : TmdbItem>(
    @SerializedName("results")
    val items: MutableList<T>,
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val pages: Int
)



class VideoWrapper(
    @SerializedName("results")
    val videos: List<Video>
)

class ItemListWrapper<T : TmdbItem>(
    @SerializedName("results")
    val items: List<T>
)

class CreditWrapper(
    val cast: List<Cast>,
    val crew: List<Crew>
)

class GenreWrapper(
    val genres: List<Genre>,
    @SerializedName("status_message")
    val statusMessage: String?,
    @SerializedName("status_code")
    val statusCode: Int?,
    val success: Boolean?,
)

interface TmdbItem : Parcelable {
    val id: Long
    val overview: String
    val releaseDate: String?
    val posterPath: String?
    val backdropPath: String?
    val name: String
    val voteAverage: Double
    val genreIds: ArrayList<String>
}

@Parcelize
data class Movie(
    override val id: Long = 0,
    override val overview: String ="",
    @SerializedName("release_date")
    override val releaseDate: String? ="",
    @SerializedName("poster_path")
    override val posterPath: String? ="",
    @SerializedName("backdrop_path")
    override val backdropPath: String? ="",
    @SerializedName("title")
    override val name: String = "",
    @SerializedName("vote_average")
    override val voteAverage: Double = 0.0,
    @SerializedName("genre_ids")
    override val genreIds: ArrayList<String> = arrayListOf(),
) : TmdbItem {


    override fun equals(other: Any?): Boolean {
        if (other is Movie) {
            if (
                id == other.id
                && overview == other.overview
                && releaseDate == other.releaseDate
                && posterPath == other.posterPath
                && backdropPath == other.backdropPath
                && name == other.name
                && voteAverage == other.voteAverage
            )
                return true
        }
        return false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

@Parcelize
data class TVShow(
    override val id: Long,
    override val overview: String,
    @SerializedName("first_air_date")
    override val releaseDate: String?,
    @SerializedName("poster_path")
    override val posterPath: String?,
    @SerializedName("backdrop_path")
    override val backdropPath: String?,
    override val name: String,
    @SerializedName("vote_average")
    override val voteAverage: Double,
    @SerializedName("genre_ids")
    override val genreIds: ArrayList<String>,
) : TmdbItem {

    override fun equals(other: Any?): Boolean {
        if (other is TVShow) {
            if (
                id == other.id
                && overview == other.overview
                && releaseDate == other.releaseDate
                && posterPath == other.posterPath
                && backdropPath == other.backdropPath
                && name == other.name
                && voteAverage == other.voteAverage
            )
                return true
        }
        return false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
@Parcelize
data class Genre(
    @SerializedName("id")
    val genreId: Long,
    val name: String,
):Parcelable

@Parcelize
data class Person(
    @SerializedName("birthday")
    val birthDay: String?,
    @SerializedName("deathday")
    val deathDay: String?,
    val id: Int,

    @SerializedName("also_known_as")
    val alsoKnownAs: List<String>?,

    val biography: String,

    @SerializedName("place_of_birth")
    val placeOfBirth: String?
) : Parcelable

@Parcelize
data class Video(
    val id: String,
    val name: String,
    val site: String, //youtube/vimeo/facebook/inst

    @SerializedName("key")
    val videoId: String,

    val type: String

) : Parcelable {
    companion object {
        private const val SITE_YOUTUBE = "YouTube"
        private const val YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v="
        private const val YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi"


        fun getVideoUrl(video: Video): String =
            if (video.site.equals(SITE_YOUTUBE, ignoreCase = true))
                YOUTUBE_VIDEO_URL + video.videoId
            else ""

        fun getVideoThumbnail(video: Video): String =
            if (video.site.equals(SITE_YOUTUBE, ignoreCase = true))
                YOUTUBE_THUMBNAIL_URL + video.videoId +".jpg"
            else ""
    }
}

interface Credit : Parcelable {
    val id: Any
    val credit: String
    val name: String
    val profilePath: String?
}

@Parcelize
class Cast(
    @SerializedName("character")
    override val credit: String,
    override val name: String,
    @SerializedName("profile_path")
    override val profilePath: String?,
    override val id: Int) : Credit

@Parcelize
class Crew(
    @SerializedName("job")
    override val credit: String,
    override val name: String,
    @SerializedName("profile_path")
    override val profilePath: String?,
    override val id: String) : Credit
