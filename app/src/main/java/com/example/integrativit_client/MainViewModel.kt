package com.example.integrativit_client

import CommandRequest
import InvokedBy
import TargetObject
import UserId
import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.integrativit_client.models.*
import com.example.integrativit_client.network.MoviesApi
import com.example.integrativit_client.network.MoviesPagingSource
import com.example.integrativit_client.ui.pages.ListType
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

const val USER_TAG = "com.integrative.user.tag"

@HiltViewModel
class MainViewModel @Inject constructor(
    arguments: SavedStateHandle,
    private val sharedPreferences: SharedPreferences,
    private val moviesApi: MoviesApi,

    ) : ViewModel() {

    val myUser = MutableStateFlow(MyUser())
    val userResponse = MutableStateFlow(UserResponse())


    var nowPlayingSource: MoviesPagingSource? = null
    var topRatedSource: MoviesPagingSource? = null
    var upcomingSource: MoviesPagingSource? = null


    val upcoming: Flow<PagingData<MovieResponse>> =
        Pager(PagingConfig(pageSize = 10, enablePlaceholders = true)) {
            MoviesPagingSource(
                fetchFunc = { page ->
                    moviesApi.getUpcomingMovies(page = page,
                        userEmail = myUser.value.email,
                        size = 10)
                },
                favIds = favIds.value,
                wishIds = wishIds.value
            ).also { upcomingSource = it }
        }.flow.cachedIn(viewModelScope)


    val nowPlaying: Flow<PagingData<MovieResponse>> =
        Pager(PagingConfig(pageSize = 10, enablePlaceholders = true)) {
            MoviesPagingSource(
                fetchFunc = { page ->
                    moviesApi.getNowPlayingMovies(page = page,
                        userEmail = myUser.value.email,
                        size = 10)
                },
                favIds = favIds.value,
                wishIds = wishIds.value
            ).also { nowPlayingSource = it }
        }.flow.cachedIn(viewModelScope)

    val topRated: Flow<PagingData<MovieResponse>> =
        Pager(PagingConfig(pageSize = 10, enablePlaceholders = true)) {
            MoviesPagingSource(
                fetchFunc = { page ->
                    moviesApi.getTopRatedMovies(page = page,
                        userEmail = myUser.value.email,
                        size = 10)
                },
                favIds = favIds.value,
                wishIds = wishIds.value
            ).also { topRatedSource = it }
        }.flow.cachedIn(viewModelScope)

    private val favIds = MutableStateFlow(listOf<String>())
    private val wishIds = MutableStateFlow(listOf<String>())

    val myWishList = MutableStateFlow(listOf<MovieResponse>())
    val myFavorite = MutableStateFlow(listOf<MovieResponse>())

    fun initPage() {
        getUser()
        try {
            updateWishList()
            updateFavoriteList()
        } catch (e: java.lang.Exception) { }
        viewModelScope.launch {
            myFavorite.collect { list ->
                favIds.value = list.map { it.objectId.internalObjectId }
                invalidateAll()
            }
        }
        viewModelScope.launch {
            favIds.collect {
                invalidateAll()

            }
        }
        viewModelScope.launch {
            wishIds.collect {
                invalidateAll()
            }
        }

        viewModelScope.launch {
            myWishList.collect { list ->
                wishIds.value = list.map { it.objectId.internalObjectId }
                invalidateAll()
            }
        }
    }

    var curList = MutableStateFlow(ListType.Up)

    fun updateUser(newUser: MyUser) {
        myUser.value = newUser
    }

    private fun addToFavorite(id: ObjectId) {
        viewModelScope.launch {
            try {
                moviesApi.addToFavorite(
                    value =
                    CommandRequest(
                        targetObject = TargetObject(objectId = id),
                        invokedBy = InvokedBy(userId = UserId(email = myUser.value.email))
                    )
                )
                updateFavoriteList()
                favIds.value = myFavorite.value.map { it.objectId.internalObjectId }

            } catch (e: java.lang.Exception) {
            }
        }
    }

    private fun addToWish(id: ObjectId) {
        viewModelScope.launch {
            try {
                moviesApi.addToWish(
                    value =
                    CommandRequest(
                        targetObject = TargetObject(objectId = id),
                        invokedBy = InvokedBy(userId = UserId(email = myUser.value.email))
                    )
                )
                updateWishList()
                wishIds.value = myWishList.value.map { it.objectId.internalObjectId }
            } catch (e: java.lang.Exception) {
            }
        }
    }

    private fun removeFromFavorite(id: ObjectId) {
        viewModelScope.launch {
            try {
                moviesApi.removeFromFavorite(
                    value =
                    CommandRequest(
                        targetObject = TargetObject(objectId = id),
                        invokedBy = InvokedBy(userId = UserId(email = myUser.value.email))
                    )
                )
                updateFavoriteList()
                favIds.value = myFavorite.value.map { it.objectId.internalObjectId }
            } catch (e: java.lang.Exception) {
            }
        }
    }

    init {
        getUser()
    }

    private fun removeFromWish(id: ObjectId) {
        viewModelScope.launch {
            try {
                moviesApi.removeFromWish(
                    value =
                    CommandRequest(
                        targetObject = TargetObject(objectId = id),
                        invokedBy = InvokedBy(userId = UserId(email = myUser.value.email))
                    )
                )
                updateWishList()
                wishIds.value = myWishList.value.map { it.objectId.internalObjectId }


            } catch (e: java.lang.Exception) {
            }
        }
    }

    private fun invalidateAll() {
        upcomingSource?.invalidate()
        topRatedSource?.invalidate()
        nowPlayingSource?.invalidate()
        println("invalidate all ")
    }

    private fun updateFavoriteList() {
        viewModelScope.launch {
            try {
                myFavorite.emit(moviesApi.getAllFavorite(
                    value =
                    CommandRequest(
                        targetObject = TargetObject(objectId = ObjectId()),
                        invokedBy = InvokedBy(userId = UserId(email = myUser.value.email))
                    )
                )?.map { it.also { it.objectDetails?.isFavorite = true } } ?: listOf())
                favIds.value = myFavorite.value.map { it.objectId.internalObjectId }

            } catch (e: java.lang.Exception) {
            }
        }
    }

    private fun updateWishList() {
        viewModelScope.launch {
            try {
                myWishList.emit(moviesApi.getAllWish(
                    value =
                    CommandRequest(
                        targetObject = TargetObject(objectId = ObjectId()),
                        invokedBy = InvokedBy(userId = UserId(email = myUser.value.email))
                    )
                )?.map { it.also { it.objectDetails?.isWish = true } } ?: listOf())
                wishIds.value = myWishList.value.map { it.objectId.internalObjectId }
            } catch (e: java.lang.Exception) {
            }
        }
    }


    fun saveUser() {
        val gson = Gson()
        val toSave =
            gson.toJson(myUser.value, MyUser::class.java)
        sharedPreferences.edit().putString(USER_TAG, toSave).apply()
    }

    fun getUser() {
        val gson = Gson()
        val temp = sharedPreferences.getString(USER_TAG, "")
        myUser.value = gson.fromJson(temp, MyUser::class.java) ?: MyUser()
    }

    fun login(runWhenSuccess: () -> Unit, runWhenFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                userResponse.value = moviesApi.login(email = myUser.value.email) ?: UserResponse()
                runWhenSuccess()

            } catch (e: java.lang.Exception) {
                runWhenFailure()
            }
        }
        saveUser()
    }

    fun signUp() {
        viewModelScope.launch {
            try {
                userResponse.value = moviesApi.signup(myUser.value)
            } catch (e: java.lang.Exception) {
            }
        }
    }

    fun WishListButtonTapped(movie: MovieResponse) {
        if (movie.objectDetails?.isWish == true) {
            removeFromWish(movie.objectId)
            movie.objectDetails.isWish = false
        } else {
            addToWish(movie.objectId)
            movie.objectDetails?.isWish = true
        }
    }

    fun favoriteButtonTapped(movie: MovieResponse) {
        if (movie.objectDetails?.isFavorite == true) {
            removeFromFavorite(movie.objectId)
            movie.objectDetails.isFavorite = false
        } else {
            addToFavorite(movie.objectId)
            movie.objectDetails?.isFavorite = true
        }
    }

    fun onListTapped(type: ListType) {
        curList.value = type
    }


}



