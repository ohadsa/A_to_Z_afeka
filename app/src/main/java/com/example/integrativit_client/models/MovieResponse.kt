package com.example.integrativit_client.models

data class MovieResponse(
   val objectId :ObjectId = ObjectId() ,
   val type :String ="",
   val alias: String ="",
   val objectDetails : MyMovie =MyMovie()
)

data class MyMovie(
    val overview :String ="",
    val release_date :String ="",
    val poster_path :String = "",
    val backdrop_path :String = "",
    val title : String ="",
    val vote_average : Double =0.0
) {
    var isFavorite: Boolean = false
    var isWish: Boolean = false
}

data class ObjectId(
    val superApp :String = "2023.ohad.saada",
    val internalObjectId :String ="2294"
)


/*
{
        "objectId": {
            "superApp": "2023.ohad.saada",
            "internalObjectId": "187"
        },
        "type": "movie",
        "alias": "",
        "active": true,
        "createdAt": "2023-01-14T15:52:55.159+00:00",
        "createdBy": {
            "superapp": "2023.ohad.saada",
            "email": "testadmin@example.com"
        },
        "objectDetails": {
            "id": 315162,
            "overview": "Puss in Boots discovers that his passion for adventure has taken its toll: He has burned through eight of his nine lives, leaving him with only one life left. Puss sets out on an epic journey to find the mythical Last Wish and restore his nine lives.",
            "release_date": "2022-12-21",
            "poster_path": "/1NqwE6LP9IEdOZ57NCT51ftHtWT.jpg",
            "backdrop_path": "/r9PkFnRUIthgBp2JZZzD380MWZy.jpg",
            "title": "Puss in Boots: The Last Wish",
            "vote_average": 8.6,
            "genre_ids": [
                "16",
                "28",
                "12",
                "35",
                "10751",
                "14"
            ]
        }
    },
 */