package com.ohadsa.a_to_z

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ohadsa.a_to_z.fragments.BuyCreditPlan
import com.ohadsa.a_to_z.network.MoviesPagingSource
import com.ohadsa.a_to_z.ui.pages.ListType
import com.ohadsa.a_to_z.models.*
import com.ohadsa.a_to_z.network.RemoteApi
import com.ohadsa.a_to_z.network.SearchPagingSource
import com.ohadsa.a_to_z.ui.pages.PremiumPlan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    arguments: SavedStateHandle,
    private val auth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences,
    private val repo: RemoteApi,
    private val realTimeDB: FirebaseDatabase,
) : ViewModel() {

    private val myUserId = auth.currentUser?.uid
    val userData = MutableStateFlow(MyUser())
    private var nowPlayingSource: MoviesPagingSource? = null
    var topRatedSource: MoviesPagingSource? = null
    var upcomingSource: MoviesPagingSource? = null
    var querySource: MoviesPagingSource? = null
    val query = MutableStateFlow("")


    val queryMovies: Flow<PagingData<Movie>> =
        Pager(PagingConfig(pageSize = 10, enablePlaceholders = true)) {
            MoviesPagingSource(
                fetchFunc = { page ->
                    repo.searchMovies(page ?: 1, query.value)
                },
            ).also { querySource = it }
        }.flow.cachedIn(viewModelScope)

    val upcoming: Flow<PagingData<Movie>> =
        Pager(PagingConfig(pageSize = 10, enablePlaceholders = true)) {
            MoviesPagingSource(
                fetchFunc = { page ->
                    repo.upComingMovies(page ?: 1)
                },
            ).also { upcomingSource = it }
        }.flow.cachedIn(viewModelScope)


    val nowPlaying: Flow<PagingData<Movie>> =
        Pager(PagingConfig(pageSize = 10, enablePlaceholders = true)) {
            MoviesPagingSource(
                fetchFunc = { page ->
                    repo.latestMovies(page ?: 1)
                },
            ).also { nowPlayingSource = it }
        }.flow.cachedIn(viewModelScope)

    val topRated: Flow<PagingData<Movie>> =
        Pager(PagingConfig(pageSize = 10, enablePlaceholders = true)) {
            MoviesPagingSource(
                fetchFunc = { page ->
                    repo.topRatedMovies(page = page ?: 1)
                },
            ).also { topRatedSource = it }
        }.flow.cachedIn(viewModelScope)

    val favIds = MutableStateFlow(listOf<Long>())
    val wishIds = MutableStateFlow(listOf<Long>())

    val myWishList = MutableStateFlow(listOf<Movie>())
    val myFavorite = MutableStateFlow(listOf<Movie>())
    val isPremium: StateFlow<Boolean> = userData
        .map { (((it.premium)) > Date().time) }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)


    fun initPage() {
        try {
            updateWishList()
            updateFavoriteList()
        } catch (e: java.lang.Exception) {
        }
        viewModelScope.launch {
            myFavorite.collect { list ->
                favIds.value = list.map { it.id }
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
                wishIds.value = list.map { it.id }
                invalidateAll()
            }
        }
    }

    private fun initUser() {
        myUserId?.let {
            realTimeDB.getReference("users").child(myUserId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        userData.value = MyUser(
                            avatar = snapshot.child("avatar").getValue(String::class.java) ?: "",
                            favoriteCredit = snapshot.child("favoriteCredit")
                                .getValue(Int::class.java) ?: 0,
                            wishCredit = snapshot.child("wishCredit").getValue(Int::class.java)
                                ?: 0,
                            username = snapshot.child("username").getValue(String::class.java)
                                ?: "",
                            premium = snapshot.child("premium").getValue(Long::class.java)
                                ?: -1
                        )
                    }

                    override fun onCancelled(error: DatabaseError) = Unit
                })
        }
    }


    private fun updateWishList() {
        myUserId?.let {
            realTimeDB.getReference("users").child(myUserId).child(WISH_TAG)
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snap: DataSnapshot, prev: String?) {
                        snap.key?.let { key ->
                            if (!wishIds.value.contains(snap.key?.toLong()))
                                wishIds.value = wishIds.value + key.toLong()
                        }

                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        snapshot.key?.let { key ->
                            if (wishIds.value.contains(snapshot.key?.toLong()))
                                wishIds.value = wishIds.value - key.toLong()
                        }
                    }

                    override fun onChildChanged(snap: DataSnapshot, prev: String?) = Unit
                    override fun onChildMoved(snap: DataSnapshot, prev: String?) = Unit
                    override fun onCancelled(error: DatabaseError) = Unit
                })
        }
    }

    var curList = MutableStateFlow(ListType.Up)


    private fun invalidateAll() {
        upcomingSource?.invalidate()
        topRatedSource?.invalidate()
        nowPlayingSource?.invalidate()
        initUser()
    }

    private fun updateFavoriteList() {
        myUserId?.let {
            realTimeDB.getReference("users").child(myUserId).child(FAV_TAG)
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        snapshot.key?.let { key ->
                            if (!favIds.value.contains(snapshot.key?.toLong()))
                                favIds.value = favIds.value + listOf(key.toLong())
                        }

                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?,
                    ) = Unit

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        snapshot.key?.let { key ->
                            if (favIds.value.contains(snapshot.key?.toLong()))
                                favIds.value = favIds.value - key.toLong()
                        }
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) =
                        Unit

                    override fun onCancelled(error: DatabaseError) = Unit
                })
        }
    }

    init {
        initUser()
        getFavFromDB()
        getWishFromDB()
    }

    private fun getWishFromDB() {
        myUserId?.let {
            realTimeDB.getReference("users").child(myUserId).child(WISH_TAG)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snap: DataSnapshot in snapshot.children) {
                            snap.key?.let {
                                viewModelScope.launch {
                                    repo.movieById(it.toInt())?.let { movie ->
                                        myWishList.value = myWishList.value + movie
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    private fun getFavFromDB() {
        myUserId?.let {
            realTimeDB.getReference("users").child(it).child(FAV_TAG)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snap: DataSnapshot in snapshot.children) {
                            snap.key?.let {
                                viewModelScope.launch {
                                    repo.movieById(it.toInt())?.let { movie ->
                                        myFavorite.value = myFavorite.value + movie
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }


    fun wishListButtonTapped(movie: Movie, runIfNoCredits: () -> Unit) {
        myUserId?.let {
            if (wishIds.value.contains(movie.id)) {
                realTimeDB.getReference("users").child(myUserId).child(WISH_TAG)
                    .child(movie.id.toString())
                    .removeValue()
                updateCreditsOnDB(userData.value.wishCredit + 1, userData.value.favoriteCredit)
                myWishList.value = myWishList.value.filter { it.id != movie.id }
                invalidateAll()

            } else {
                if (userData.value.wishCredit > 0) {
                    realTimeDB.getReference("users").child(myUserId).child(WISH_TAG)
                        .child(movie.id.toString())
                        .setValue(true)
                    updateCreditsOnDB(userData.value.wishCredit - 1, userData.value.favoriteCredit)
                    myWishList.value = myWishList.value + movie
                    invalidateAll()
                } else {
                    runIfNoCredits()
                }
            }


        }
    }

    private fun updateCreditsOnDB(wishCount: Int, favCount: Int) {
        myUserId?.let {
            realTimeDB.getReference("users").child(myUserId).child("favoriteCredit")
                .setValue(favCount)
            realTimeDB.getReference("users").child(myUserId).child("wishCredit")
                .setValue(wishCount)
        }
    }

    fun favoriteButtonTapped(movie: Movie, runIfNoCredits: () -> Unit) {
        myUserId?.let {
            if (favIds.value.contains(movie.id)) {
                realTimeDB.getReference("users").child(myUserId).child(FAV_TAG)
                    .child(movie.id.toString())
                    .removeValue()
                updateCreditsOnDB(userData.value.wishCredit, userData.value.favoriteCredit + 1)
                myFavorite.value = myFavorite.value.filter { it.id != movie.id }
                invalidateAll()
            } else {
                if (userData.value.favoriteCredit > 0) {
                    realTimeDB.getReference("users").child(myUserId).child(FAV_TAG)
                        .child(movie.id.toString())
                        .setValue(true)
                    updateCreditsOnDB(userData.value.wishCredit, userData.value.favoriteCredit - 1)
                    myFavorite.value = myFavorite.value + movie
                    invalidateAll()
                } else {
                    runIfNoCredits()
                }
            }
        }
    }

    fun onListTapped(type: ListType) {
        curList.value = type
    }

    fun logoutTapped() {
        auth.signOut()
    }

    fun goPremiumTapped(plan: PremiumPlan) {
        myUserId?.let {
            when (plan) {
                PremiumPlan.TreeMonth -> realTimeDB.getReference("users").child(myUserId)
                    .child("premium")
                    .setValue(Date().time + 7.884e+9 /* 3 MONTH */)
                PremiumPlan.OneMonth -> realTimeDB.getReference("users").child(myUserId)
                    .child("premium")
                    .setValue(Date().time + 2.628e+9/* 1 MONTH */)
                PremiumPlan.OneYear -> realTimeDB.getReference("users").child(myUserId)
                    .child("premium")
                    .setValue(Date().time + 3.154e+10/* 1 YEAR */)
            }
        }


    }

    fun notifyQueryChanged(newValue: String) {
        query.value = newValue
        querySource?.invalidate()
    }

    fun buyCreditTapped(plan: BuyCreditPlan) {
        myUserId?.let { id ->
            val creditToAdd = when (plan) {
                BuyCreditPlan.FiveCredits -> 5
                BuyCreditPlan.TwentyCredits -> 20
                BuyCreditPlan.HundredCredits -> 100
            }
            realTimeDB.getReference("users").child(id).child("favoriteCredit")
                .setValue(userData.value.favoriteCredit + creditToAdd)
            realTimeDB.getReference("users").child("wishCredit")
                .setValue(userData.value.wishCredit + creditToAdd)
        }
    }
}

const val FAV_TAG = "favorites"
const val WISH_TAG = "wish_list"


