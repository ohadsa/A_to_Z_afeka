package com.example.integrativit_client.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyUser(
    val username: String = "",
    val email: String = "",
    val avatar: String = "",
    val role: UserRole = UserRole.MINIAPP_USER,
):Parcelable {
    fun allFilled(): Boolean {
        return username.isNotEmpty() && email.isNotEmpty() && avatar.isNotEmpty()
    }
}

enum class UserRole {
    SUPERAPP_USER, ADMIN, MINIAPP_USER
}


/*
"username": "test",
"email": "test@example.com",
"avatar": "testedAvatar",
"role": "SUPERAPP_USER"
*/