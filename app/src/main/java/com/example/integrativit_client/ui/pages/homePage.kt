package com.example.integrativit_client.ui.pages


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.integrativit_client.MainViewModel
import com.example.integrativit_client.R
import com.example.integrativit_client.models.MovieResponse
import com.example.integrativit_client.ui.custome.MovieItem
import com.example.integrativit_client.ui.custome.MoviePage
import com.example.integrativit_client.ui.generic.*
import com.example.integrativit_client.ui.theme.MyColors
import com.example.integrativit_client.ui.theme.generic.MyText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GamePage(
    viewModel: MainViewModel,
    onListTapped: () -> Unit,
) {


    val upcoming = viewModel.upcoming.collectAsLazyPagingItems()
    val nowPlaying = viewModel.nowPlaying.collectAsLazyPagingItems()
    val topRated = viewModel.topRated.collectAsLazyPagingItems()
    val myFavorite by viewModel.myFavorite.collectAsState()
    val wish by viewModel.myWishList.collectAsState()

    viewModel.initPage()

    var curItem by remember {
        mutableStateOf(if (upcoming.itemCount > 0) upcoming[0]
            ?: MovieResponse() else MovieResponse())
    }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    fun showSheet(show: Boolean) =
        scope.launch {
            if (show) sheetState.show()
            else sheetState.hide()
        }
    ModalBottomSheetLayout(
        sheetShape = MaterialTheme.shapes.large.copy(
            topStart = CornerSize(12.dp), topEnd = CornerSize(12.dp)
        ),
        sheetState = sheetState,
        sheetContent = {
            MoviePage(
                curItem = curItem,
                onAddToWishListTapped = { movie ->
                    viewModel.WishListButtonTapped(movie)
                },
                onAddToFavoriteTapped = { movie ->
                    viewModel.favoriteButtonTapped(movie)

                },
                isItemInFavorite = myFavorite.contains(curItem),
                isItemInWish = wish.contains(curItem),
            )
        },
    ) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(Modifier.fillMaxWidth()) {
                DrawableImage(
                    id = R.drawable.tmdb,
                    svg = false,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(100.dp),
                )
            }
            if (myFavorite.isNotEmpty()) {
                TextWithArrow(
                    leadingIcon = R.drawable.star,
                    text = "My favorite movies",
                    font = MyFont.Heading4,
                    lineHeight = MyFont.Heading4.lineHeight,
                    color = MyColors.darkGray,
                ) {
                    viewModel.onListTapped(ListType.Fav)
                    onListTapped()
                }
            }
            LazyRow {
                itemsIndexed(myFavorite, { _, it -> it.objectId.internalObjectId })
                { index, item ->
                    LandScapeMovieCell(item) {
                        curItem = item
                        showSheet(true)
                    }
                }
            }
            if (myFavorite.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (wish.isNotEmpty()) {
                TextWithArrow(
                    leadingIcon = R.drawable.bookmark,
                    text = "My Wish list",
                    font = MyFont.Heading4,
                    lineHeight = MyFont.Heading4.lineHeight,
                    color = MyColors.darkGray,
                ) {
                    viewModel.onListTapped(ListType.Wish)
                    onListTapped()
                }
            }

            LazyRow {
                itemsIndexed(wish, { _, it -> it.objectId.internalObjectId }) { index, item ->
                    LandScapeMovieCell(item) {
                        curItem = item
                        showSheet(true)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            TextWithArrow(
                leadingIcon = R.drawable.movie_flat,
                text = "All upcoming movies",
                font = MyFont.Heading4,
                lineHeight = MyFont.Heading4.lineHeight,
                color = MyColors.darkGray,
            ) {
                viewModel.onListTapped(ListType.Up)
                onListTapped()
            }
            LazyRow {
                itemsIndexed(upcoming,
                    { _, it -> it.objectId.internalObjectId }) { index, item ->
                    if (item != null)
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            MovieItem(modifier = Modifier
                                .height(160.dp),
                                item.objectDetails.poster_path.toPath(),
                                item.objectDetails.isFavorite)
                            {
                                curItem = item
                                showSheet(true)

                            }
                        }

                }
            }


            Spacer(modifier = Modifier.height(32.dp))
            TextWithArrow(
                leadingIcon = R.drawable.badge,
                text = "Top rated from all Times",
                font = MyFont.Heading4,
                lineHeight = MyFont.Heading4.lineHeight,
                color = MyColors.darkGray,
            ) {
                viewModel.onListTapped(ListType.Top)
                onListTapped()
            }
            LazyRow {
                itemsIndexed(topRated,
                    { _, it -> it.objectId.internalObjectId }) { index, item ->
                    if (item != null)
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            MovieItem(modifier = Modifier
                                .height(160.dp),
                                item.objectDetails.poster_path.toPath(),
                                item.objectDetails.isFavorite
                            ) {
                                curItem = item
                                showSheet(true)

                            }
                        }

                }
            }





            Spacer(modifier = Modifier.height(36.dp))
            TextWithArrow(
                leadingIcon = R.drawable.film_reel,
                text = "Today top watched",
                font = MyFont.Heading4,
                lineHeight = MyFont.Heading4.lineHeight,
                color = MyColors.darkGray,
            ) {
                viewModel.onListTapped(ListType.Today)
                onListTapped()
            }
            LazyRow {
                itemsIndexed(nowPlaying,
                    { _, it -> it.objectId.internalObjectId }) { index, item ->
                    if (item != null)
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            MovieItem(modifier = Modifier
                                .height(160.dp),
                                item.objectDetails.poster_path.toPath(),
                                item.objectDetails.isFavorite
                            ) {
                                curItem = item
                                showSheet(true)

                            }
                        }

                }
            }
            Spacer(modifier = Modifier.height(36.dp))

        }
    }
}

@Composable
fun LandScapeMovieCell(
    item: MovieResponse,
    onTapped: () -> Unit,
) {
    Box {
        Column {
            MovieItem(modifier = Modifier
                .height(160.dp)
                .width(280.dp),
                item.objectDetails.backdrop_path.toPath(), item.objectDetails.isFavorite, big = true
            ) {
                onTapped()
            }
            MyText(
                modifier = Modifier.padding(start = 12.dp),
                text = item.objectDetails.title,
                font = MyFont.Body14Italic,
                color = MyColors.indigoDark)

        }
        CircularProgressbar(
            number = item.objectDetails.vote_average.toFloat() * 10,
            size = 28.dp,
            indicatorThickness = 6.dp,
            modifier = Modifier
                .padding(bottom = 8.dp, end = 18.dp)
                .align(BottomEnd))
    }
}

enum class ListType(val text: String) {
    Fav("My favorite movies"), Wish("My Wish list"), Up("All upcoming movies "), Top("Top rated from all Times"), Today(
        "Today top watched")
}


fun String.toPath(): String {
    return "$IMAGE_BASE_UR/${this}"
}

const
val IMAGE_BASE_UR = "https://image.tmdb.org/t/p/w500/"
