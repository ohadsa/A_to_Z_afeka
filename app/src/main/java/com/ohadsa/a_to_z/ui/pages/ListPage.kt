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
import com.example.integrativit_client.MainViewModel
import com.ohadsa.a_to_z.R
import com.ohadsa.a_to_z.models.MovieResponse
import com.ohadsa.a_to_z.ui.custome.MovieItem
import com.ohadsa.a_to_z.ui.custome.MoviePage
import com.ohadsa.a_to_z.ui.generic.CircularProgressbar
import com.ohadsa.a_to_z.ui.generic.ClickableTopBar
import com.ohadsa.a_to_z.ui.generic.DrawableImage
import com.ohadsa.a_to_z.ui.generic.MyFont
import com.ohadsa.a_to_z.ui.theme.MyColors
import com.ohadsa.a_to_z.ui.generic.MyText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListPage(
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {
    val curList by viewModel.curList.collectAsState()

    val myWishList by viewModel.myWishList.collectAsState()
    val myFavorite by viewModel.myFavorite.collectAsState()

    var curItem by remember {
        mutableStateOf(MovieResponse())
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
                isItemInFavorite = myFavorite.contains(curItem),
                isItemInWish = myWishList.contains(curItem),
                onAddToWishListTapped = { movie ->
                    viewModel.WishListButtonTapped(movie)
                },
                onAddToFavoriteTapped = { movie ->
                    viewModel.favoriteButtonTapped(movie)
                }
            )
        },
    ) {
        ClickableTopBar(leftId = R.drawable.left_arrow, left = null, onLeft = onBack)

        when (curList) {
            ListType.Fav -> {

                ListMovieCell(title = curList.text, myFavorite) {
                    curItem = it
                    showSheet(true)
                }
            }
            ListType.Wish -> {
                val myWishList by viewModel.myWishList.collectAsState()
                ListMovieCell(title = curList.text, myWishList) {
                    curItem = it
                    showSheet(true)
                }
            }

            ListType.Up ->{
                val list = viewModel.upcoming.collectAsLazyPagingItems()
                PagingMovieListCell(title = curList.text, list) {
                    curItem = it
                    showSheet(true)
                }
            }
            ListType.Top ->{
                val list = viewModel.topRated.collectAsLazyPagingItems()
                PagingMovieListCell(title = curList.text, list) {
                    curItem = it
                    showSheet(true)
                }
            }
            ListType.Today -> {
                val list = viewModel.nowPlaying.collectAsLazyPagingItems()
                PagingMovieListCell(title = curList.text, list) {
                    curItem = it
                    showSheet(true)
                }
            }

        }
    }
}

@Composable
fun PagingMovieListCell(
    title: String,
    list: LazyPagingItems<MovieResponse>,
    onTapped: (MovieResponse) -> Unit,
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
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                MyText(
                    modifier = Modifier.align(Alignment.Center),
                    text = title,
                    font = MyFont.Heading4,
                    color = MyColors.indigoPrimary,
                    lineHeight = MyFont.Heading4.lineHeight
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

        }
        itemsIndexed(list, { _, it -> it.objectId.internalObjectId })
        { index, item ->
            if (item != null ) {
                Box {
                    Column {
                        MovieItem(modifier = Modifier
                            .height(240.dp)
                            .fillMaxWidth(),
                            item.objectDetails?.backdrop_path?.toPath()?:"",
                            item.objectDetails?.isFavorite?:false
                        ) {
                            onTapped(item)
                        }
                        MyText(
                            modifier = Modifier.padding(start = 12.dp),
                            text = item.objectDetails?.title?:"",
                            font = MyFont.Body14Italic,
                            color = MyColors.indigoDark)
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    CircularProgressbar(
                        number = (item.objectDetails?.vote_average?.toFloat()?:9f) * 10,
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
    title: String,
    list: List<MovieResponse>,
    onTapped: (MovieResponse) -> Unit,
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
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                MyText(
                    modifier = Modifier.align(Alignment.Center),
                    text = title,
                    font = MyFont.Heading4,
                    color = MyColors.indigoPrimary,
                    lineHeight = MyFont.Heading4.lineHeight
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

        }
        itemsIndexed(list, { _, it -> it.objectId.internalObjectId })
        { index, item ->
            Box {
                Column {
                    MovieItem(modifier = Modifier
                        .height(240.dp)
                        .fillMaxWidth(),
                        item.objectDetails?.backdrop_path?.toPath()?:"",
                        item.objectDetails?.isFavorite?:false
                    ) {
                        onTapped(item)

                    }
                    MyText(
                        modifier = Modifier.padding(start = 12.dp),
                        text = item.objectDetails?.title?:"",
                        font = MyFont.Body14Italic,
                        color = MyColors.indigoDark)
                    Spacer(modifier = Modifier.height(24.dp))
                }
                CircularProgressbar(
                    number = (item.objectDetails?.vote_average?.toFloat()?:9f) * 10,
                    size = 32.dp,
                    indicatorThickness = 8.dp,
                    modifier = Modifier
                        .padding(bottom = 32.dp, end = 18.dp)
                        .align(Alignment.BottomEnd))
            }

        }
    }
}