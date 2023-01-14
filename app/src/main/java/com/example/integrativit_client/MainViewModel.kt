package com.example.integrativit_client

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.integrativit_client.models.MyUser
import com.example.integrativit_client.models.UserResponse
import com.example.integrativit_client.network.MoviesAPi
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val USER_TAG = "com.integrative.user.tag"

@HiltViewModel
class MainViewModel @Inject constructor(
    arguments: SavedStateHandle,
    private val sharedPreferences: SharedPreferences,
    private val moviesAPi: MoviesAPi,
) : ViewModel() {


    val myUser = MutableStateFlow(MyUser())
    val userResponse = MutableStateFlow<UserResponse?>(null)

    fun updateUser(newUser: MyUser) {
        myUser.value = newUser
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
                userResponse.value = moviesAPi.login(myUser.value.email)
                runWhenSuccess()
            }
            catch (e:java.lang.Exception){
                runWhenFailure()
            }
        }
        saveUser()
    }

    fun signUp() {
        viewModelScope.launch {
            userResponse.value = moviesAPi.signup(myUser.value)
        }
    }

    init {
        getUser()
    }

}



