package com.ohadsa.a_to_z.network


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ohadsa.a_to_z.models.ItemWrapper
import com.ohadsa.a_to_z.models.Movie
import com.ohadsa.a_to_z.models.MovieResponse
import javax.inject.Inject


class SearchPagingSource @Inject constructor(
    private val fetchFunc: suspend (page: Int?, query: String) -> Result<ItemWrapper<Movie>>?,
    private val query: String,
) : PagingSource<Int, Movie>() {
    var first = true

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val key = (if (first) 1 else params.key) ?: throw Exception()
            println("page = $key")
            first = false
            when (val data = fetchFunc(key, query)) {
                is Failure, null -> throw Exception()
                is Success -> {
                    val list = data.data.items
                    LoadResult.Page(list, null, key + 1)
                }
            }
        } catch (e: java.lang.Exception) {
            LoadResult.Page(emptyList(), null, null)
        }
    }
}


