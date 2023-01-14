package com.example.integrativit_client.models

data class UserResponse(
    val role: UserRole,
    val avatar: String,
    val userName: String,
    val userId: UserId,
    val createdAt: String,
    val details: UserDetails,
)

data class UserDetails(
    val wishList: List<String>,
    val favorite: List<String>,
    val watched: List<String>,
)

data class UserId(
    val superapp: String,
    val email: String,
)

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