package com.ohadsa.a_to_z.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserResponse(
    val userName: String ="",
    val avatar: String ="",
    val details: UserDetails = UserDetails(),
):Parcelable


@Parcelize
data class UserDetails(
    val wishList: List<String> = emptyList(),
    val favorite: List<String> = emptyList(),
    val watched: List<String> = emptyList(),
):Parcelable



/*
{
    "role": "ADMIN",
    "avatar": "testedAvatar",
    "username": "testadmin",
    "userId": {
        "superapp": "2023.ohad.saada",
        "email": "testadmin@example.com"
    },
    "createdAt": "2023-01-14T16:07:25.825+00:00",
    "details": {
        "wishList": [],
        "favorite": [],
        "watched": []
    }
}
 */