package com.ohadsa.a_to_z.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.integrativit_client.ui.generic.CostumeDivider
import com.ohadsa.a_to_z.MainViewModel
import com.ohadsa.a_to_z.R
import com.ohadsa.a_to_z.fragments.BuyCreditScreen
import com.ohadsa.a_to_z.models.Movie
import com.ohadsa.a_to_z.ui.custome.MovieItem
import com.ohadsa.a_to_z.ui.custome.MoviePage
import com.ohadsa.a_to_z.ui.generic.*
import com.ohadsa.a_to_z.ui.theme.MyColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListPage(
    modifier: Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {
    val curList by viewModel.curList.collectAsState()

    val myWishList by viewModel.myWishList.collectAsState()
    val myFavorite by viewModel.myFavorite.collectAsState()

    val favIds by viewModel.favIds.collectAsState()
    val premium by viewModel.isPremium.collectAsState()
    val wishIds by viewModel.wishIds.collectAsState()

    var curItem by remember {
        mutableStateOf(Movie())
    }
    var openBuyPopup by remember {
        mutableStateOf(false)
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
                modifier = Modifier,
                premium=premium,
                curItem = curItem,
                isItemInFavorite = favIds.contains(curItem.id),
                isItemInWish = wishIds.contains(curItem.id),
                onAddToWishListTapped = { movie ->
                    viewModel.wishListButtonTapped(movie) {
                        openBuyPopup = true
                    }
                },
                onAddToFavoriteTapped = { movie ->
                    viewModel.favoriteButtonTapped(movie) {
                        openBuyPopup = true
                    }
                },
            )
        },
    ) {
        Column (modifier){
            ClickableTopBar(leftId = R.drawable.right_arrow,
                middle = curList.text,
                left = null,
                onLeft = onBack)
            CostumeDivider(color = MyColors.gray50, modifier = Modifier.padding(top = 16.dp))
            when (curList) {
                ListType.Fav -> {
                    ListMovieCell(myFavorite,favIds,wishIds) {
                        curItem = it
                        showSheet(true)
                    }
                }
                ListType.Wish -> {
                    ListMovieCell(myWishList,favIds,wishIds) {
                        curItem = it
                        showSheet(true)
                    }
                }

                ListType.Up -> {
                    val list = viewModel.upcoming.collectAsLazyPagingItems()
                    PagingMovieListCell(
                        list,
                        favIds,
                        wishIds,
                    ) {
                        curItem = it
                        showSheet(true)
                    }
                }
                ListType.Top -> {
                    val list = viewModel.topRated.collectAsLazyPagingItems()
                    PagingMovieListCell(
                        list,
                        favIds,
                        wishIds,
                    ) {
                        curItem = it
                        showSheet(true)
                    }
                }
                ListType.Today -> {
                    val list = viewModel.nowPlaying.collectAsLazyPagingItems()
                    PagingMovieListCell(
                        list,
                        favIds,
                        wishIds,
                    ) {
                        curItem = it
                        showSheet(true)
                    }
                }
            }
        }
        if (openBuyPopup) {
            AnimatedDialog {
                BuyCreditScreen(onBack = {  openBuyPopup = false }){
                    viewModel.buyCreditTapped(it)
                    openBuyPopup = false
                }
            }
        }
    }
}

@Composable
fun PagingMovieListCell(
    list: LazyPagingItems<Movie>,
    favIds: List<Long>,
    wishIds: List<Long>,
    onTapped: (Movie) -> Unit,
) {
    LazyColumn {
        item {
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
        }
        itemsIndexed(list, { _, it -> it.id })
        { index, item ->
            if (item != null) {
                Box {
                    Column {
                        MovieItem(modifier = Modifier
                            .height(240.dp)
                            .fillMaxWidth(),
                            item.backdropPath?.toPath() ?: "",
                            favIds.contains(item.id)
                        ) {
                            onTapped(item)
                        }
                        MyText(
                            modifier = Modifier.padding(start = 12.dp),
                            text = item.name ?: "",
                            font = MyFont.Body14Italic,
                            color = MyColors.indigoDark)
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    CircularProgressbar(
                        number = (item.voteAverage.toFloat() ?: 9f) * 10,
                        size = 32.dp,
                        indicatorThickness = 8.dp,
                        modifier = Modifier
                            .padding(bottom = 32.dp, end = 18.dp)
                            .align(Alignment.BottomEnd))
                }
            }
        }
    }
}

@Composable
fun ListMovieCell(
    list: List<Movie>,
    favIds: List<Long>,
    wishIds: List<Long>,
    onTapped: (Movie) -> Unit,
) {
    LazyColumn {
        item {
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
        }

        itemsIndexed(list, { _, it -> it.id })
        { index, item ->
            Box {
                Column {
                    MovieItem(modifier = Modifier
                        .height(240.dp)
                        .fillMaxWidth(),
                        item.backdropPath?.toPath() ?: "",
                       favIds.contains(item.id)
                    ) {
                        onTapped(item)

                    }
                    MyText(
                        modifier = Modifier.padding(start = 12.dp),
                        text = item.name ?: "",
                        font = MyFont.Body14Italic,
                        color = MyColors.indigoDark)
                    Spacer(modifier = Modifier.height(24.dp))
                }
                CircularProgressbar(
                    number = (item.voteAverage.toFloat() ?: 9f) * 10,
                    size = 32.dp,
                    indicatorThickness = 8.dp,
                    modifier = Modifier
                        .padding(bottom = 32.dp, end = 18.dp)
                        .align(Alignment.BottomEnd))
            }

        }
    }
}