package com.ohadsa.a_to_z.ui.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.integrativit_client.ui.generic.CostumeDivider
import com.ohadsa.a_to_z.MainViewModel
import com.ohadsa.a_to_z.R
import com.ohadsa.a_to_z.fragments.BuyCreditScreen
import com.ohadsa.a_to_z.login.ui.composable.EmptyStateComponent
import com.ohadsa.a_to_z.models.Movie
import com.ohadsa.a_to_z.ui.custome.MovieItem
import com.ohadsa.a_to_z.ui.custome.MoviePage
import com.ohadsa.a_to_z.ui.generic.AnimatedDialog
import com.ohadsa.a_to_z.ui.generic.ClickableTopBar
import com.ohadsa.a_to_z.ui.generic.SearchBar
import com.ohadsa.a_to_z.ui.theme.MyColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun SearchPage(
    modifier: Modifier,
    viewModel: MainViewModel,
    onBack: () -> Unit,
) {

    val favIds by viewModel.favIds.collectAsState()
    val wishIds by viewModel.wishIds.collectAsState()
    var curItem by remember {
        mutableStateOf(Movie())
    }
    var openBuyPopup by remember {
        mutableStateOf(false)
    }
    val query by viewModel.query.collectAsState()
    val premium by viewModel.isPremium.collectAsState()
    var list = viewModel.queryMovies.collectAsLazyPagingItems()

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
                premium = premium
            )
        },
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            ClickableTopBar(left = null,
                leftId = R.drawable.chevron_left,
                middle = stringResource(
                    id = R.string.movie_world),
                right = null,
                onLeft = {
                    onBack()
                })
            CostumeDivider(color = MyColors.gray50,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp))
            SearchBar(
                text = query,
                focusChanged = {},
                searchClicked = {},
                onTextChange = {

                    viewModel.notifyQueryChanged(it)
                },
                onCancel = {
                    viewModel.notifyQueryChanged("")

                })
            if (list.itemCount != 0) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(100.dp),
                    contentPadding = PaddingValues(
                        horizontal = 12.dp,
                        vertical = 16.dp,
                    ),
                    content = {
                        items(list) { item ->
                            MovieItem(modifier = Modifier
                                .height(180.dp),
                                item.posterPath?.toPath() ?: "",
                                favIds.contains(item.id)
                            ) {
                                curItem = item
                                showSheet(true)
                            }
                        }

                    }
                )
            } else
                EmptyStateComponent(
                    modifier = Modifier.fillMaxSize(),
                    drawable = R.drawable.no_results,
                    title = stringResource(id = R.string.search_empty_state),
                    description = stringResource(id = R.string.search_empty_state_description))

        }
        if (openBuyPopup) {
            AnimatedDialog {
                BuyCreditScreen(onBack = {  openBuyPopup = false }) {
                   viewModel.buyCreditTapped(it)
                    openBuyPopup = false
                }
            }
        }
    }
}

@ExperimentalFoundationApi
fun <T : Any> LazyGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    content: @Composable (value: T) -> Unit,
) {
    items(lazyPagingItems.itemCount) { index ->
        lazyPagingItems[index]?.let {
            content(it)
        }
    }
}

