package com.ohadsa.a_to_z.network


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ohadsa.a_to_z.models.MovieResponse
import javax.inject.Inject


class MoviesPagingSource @Inject constructor(
    private val fetchFunc: suspend (page: Int?) -> List<MovieResponse>?,
    private val favIds : List<String>,
    private val wishIds : List<String>,
) : PagingSource<Int, MovieResponse>() {
    var first = true

    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int? = null
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse> {
        return try {
            val key = (if (first) 0 else params.key) ?: throw Exception()
            println("page = $key")
            first = false
            val data = fetchFunc(key)
            if(data.isNullOrEmpty()) throw Exception()
            data.forEach {
                it.objectDetails?.isWish = wishIds.contains(it.objectId.internalObjectId)
                it.objectDetails?.isFavorite = favIds.contains(it.objectId.internalObjectId)
            }
            LoadResult.Page(data, null, if (data.isEmpty()) null else key + 1)
        } catch (e: java.lang.Exception) {
            LoadResult.Page(emptyList(), null, null)
        }
    }

    /*
    override suspend fun load(params: LoadParams<String>): LoadResult<Int, MyMovie> {
        try {
            val key = params.key
            if (key == null)
                LoadResult.Page(initialData, null, initialData.lastOrNull()?.id)
            else {
                val data = fetchFunc(key)
                val finalResult = data.data

                val maxId = if (data.hasMore) finalResult.lastOrNull()?.id else null
                val result = LoadResult.Page(
                    finalResult, null, maxId
                )
                dataFetched.value = true
                result
            }
        } catch (e: Exception) {
            return LoadResult.Page(emptyList(), null, null)
        }
    }

     */
}


