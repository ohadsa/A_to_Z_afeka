package com.ohadsa.a_to_z.ui.pages


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.example.integrativit_client.ui.generic.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.ohadsa.a_to_z.MainViewModel
import com.ohadsa.a_to_z.R
import com.ohadsa.a_to_z.fragments.BuyCreditScreen
import com.ohadsa.a_to_z.login.ConfirmPay
import com.ohadsa.a_to_z.models.Movie
import com.ohadsa.a_to_z.ui.custome.MovieItem
import com.ohadsa.a_to_z.ui.custome.MoviePage
import com.ohadsa.a_to_z.ui.generic.*
import com.ohadsa.a_to_z.ui.theme.MyColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomePage(
    modifier: Modifier,
    viewModel: MainViewModel,
    onLogoTapped: () -> Unit,
    onSearchTapped: () -> Unit,
    onMenuTapped: () -> Unit,
    onListTapped: () -> Unit,
) {

    val upcoming = viewModel.upcoming.collectAsLazyPagingItems()
    val nowPlaying = viewModel.nowPlaying.collectAsLazyPagingItems()
    val topRated = viewModel.topRated.collectAsLazyPagingItems()
    val myFavorite by viewModel.myFavorite.collectAsState()
    val wish by viewModel.myWishList.collectAsState()
    val user by viewModel.userData.collectAsState()
    val premium by viewModel.isPremium.collectAsState()
    val favIds by viewModel.favIds.collectAsState()
    val wishIds by viewModel.wishIds.collectAsState()
    var showPremiumScreen by remember { mutableStateOf(true) }

    viewModel.initPage()

    var curItem by remember {
        mutableStateOf(if (upcoming.itemCount > 0) upcoming[0]
            ?: Movie() else Movie())
    }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    var openBuyPopup by remember {
        mutableStateOf(false)
    }

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
                premium = premium,
                curItem = curItem,
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
                isItemInFavorite = favIds.contains(curItem.id),
                isItemInWish = wishIds.contains(curItem.id),
            )
        },
    ) {
        Column (modifier){
            ClickableTopBar(left = null,
                leftId = R.drawable.menu_bar,
                middle = stringResource(
                    id = R.string.movie_world),
                right = null,
                onLeft = {
                    onMenuTapped()
                },
                onRight = {
                    onSearchTapped()
                },
                rightId = R.drawable.search_top_bar_)
            CostumeDivider(color = MyColors.gray50,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp))
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(Modifier.fillMaxWidth()) {
                    DrawableImage(
                        id = R.drawable.tmdb,
                        svg = false,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clickableNoFeedback {
                                onLogoTapped()
                            }
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
                    itemsIndexed(myFavorite, { _, it -> it.id })
                    { index, item ->
                        LandScapeMovieCell(item, favIds) {
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
                    itemsIndexed(wish, { _, it -> it.id }) { index, item ->
                        LandScapeMovieCell(item, favIds) {
                            curItem = item
                            showSheet(true)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
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
                        { _, it -> it.id }) { index, item ->
                        if (item != null)
                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                MovieItem(modifier = Modifier
                                    .height(160.dp),
                                    item.posterPath?.toPath() ?: "",
                                    favIds.contains(item.id)
                                ) {
                                    curItem = item
                                    showSheet(true)
                                }
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
                        { _, it -> it.id }) { index, item ->
                        if (item != null)
                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                MovieItem(modifier = Modifier
                                    .height(160.dp),
                                    item.posterPath?.toPath() ?: "",
                                    favIds.contains(item.id)
                                )
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
                        { _, it -> it.id }) { index, item ->
                        if (item != null)
                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                MovieItem(modifier = Modifier
                                    .height(160.dp),
                                    item.posterPath?.toPath() ?: "",
                                    favIds.contains(item.id)
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
        if (!premium && showPremiumScreen) {

            SubscribeScreen(
                onBack = { showPremiumScreen = false },
                goPremiumTapped = { viewModel.goPremiumTapped(it) }
            )
        }
        if (openBuyPopup) {
            AnimatedDialog {
                BuyCreditScreen(onBack = { openBuyPopup = false }) {
                    viewModel.buyCreditTapped(it)
                    openBuyPopup = false
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubscribeScreen(
    onBack: () -> Unit,
    goPremiumTapped: (PremiumPlan) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    var plan by remember {
        mutableStateOf(PremiumPlan.OneYear)
    }


    fun showSheet(show: Boolean) =
        scope.launch {
            if (show) sheetState.show()
            else sheetState.hide()
        }
    ModalBottomSheetLayout(
        sheetShape = MaterialTheme.shapes.large.copy(
            topStart = CornerSize(36.dp), topEnd = CornerSize(36.dp)
        ),
        sheetState = sheetState,
        sheetContent = {
            ConfirmPay(
                price = when (plan) {
                    PremiumPlan.OneMonth -> 9.90
                    PremiumPlan.TreeMonth -> 22.5
                    PremiumPlan.OneYear -> 60.0
                },
                onChoose = {
                    showSheet(false)
                    goPremiumTapped(plan)
                },
                onBack = {
                    showSheet(false)
                }
            )
        },
    ) {
        Column(Modifier
            .background(Color.White)
            .clickableNoFeedback {  }
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally) {
            ClickableTopBar(left = null,
                leftId = R.drawable.chevron_left,
                middle = stringResource(
                    id = R.string.movie_world),
                right = null,
                onLeft = {
                    onBack()
                })
            CostumeDivider(color = MyColors.gray50,
                modifier = Modifier.padding(top = 16.dp)
            )
            Column(Modifier
                .background(MyColors.main25)
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp, bottom = 4.dp)
                .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally) {
                MyText(
                    modifier = Modifier,
                    text = stringResource(id = R.string.subscribe),
                    font = MyFont.GiantHeading,
                    color = MyColors.indigoPrimary,
                    lineHeight = MyFont.GiantHeading.lineHeight)
                MyText(
                    modifier = Modifier,
                    text = stringResource(id = R.string.subscribe_text),
                    font = MyFont.Heading5,
                    color = MyColors.gray65,
                    lineHeight = MyFont.Heading5.lineHeight)
                PlanCell {
                    plan = it
                    showSheet(true)
                }

            }
        }
    }
}

@Composable
fun PlanCell(goPremiumTapped: (PremiumPlan) -> Unit) {
    Column {
        Row(Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
            .border(2.dp, MyColors.main, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp)
            )
            .height(120.dp)
            .padding(horizontal = 12.dp)
            .clickableNoFeedback { goPremiumTapped(PremiumPlan.OneYear) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column {
                MyText(
                    text = stringResource(id = R.string.yearPlan) + "   -   " + "60$",
                    font = MyFont(
                        weight = FontWeight.ExtraBold,
                        textSize = 16.sp,
                        fontName = FontName.DMSans,
                    ),
                    color = MyColors.main,
                )
            }
            DrawableImage(id = R.drawable.gold_star,
                svg = false,
                modifier = Modifier
                    .padding(end = 12.dp, bottom = 18.dp)
                    .size(42.dp))
        }



        Row(Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
            .border(2.dp, MyColors.main, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp)
            )
            .height(120.dp)
            .padding(horizontal = 12.dp)
            .clickableNoFeedback { goPremiumTapped(PremiumPlan.TreeMonth) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column {
                MyText(
                    text = stringResource(id = R.string.treeMonth) + "  -  " + "22.5$",
                    font = MyFont(
                        weight = FontWeight.ExtraBold,
                        textSize = 16.sp,
                        fontName = FontName.DMSans,
                    ),
                    color = MyColors.main,
                )
            }
            DrawableImage(id = R.drawable.star,
                svg = false,
                modifier = Modifier
                    .padding(end = 12.dp, bottom = 18.dp)
                    .size(42.dp))
        }



        Row(Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
            .border(2.dp, MyColors.main, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp)
            )
            .height(120.dp)
            .padding(horizontal = 12.dp)
            .clickableNoFeedback { goPremiumTapped(PremiumPlan.OneMonth) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column {
                MyText(
                    text = stringResource(id = R.string.monthPlan) + "  -  " + "9.90$",
                    font = MyFont(
                        weight = FontWeight.ExtraBold,
                        textSize = 16.sp,
                        fontName = FontName.DMSans,
                    ),
                    color = MyColors.main,
                )
            }
        }
    }
}

@Composable
fun LandScapeMovieCell(
    item: Movie?,
    favIds: List<Long>,
    onTapped: () -> Unit,
) {
    if (item == null) return
    Box {
        Column {
            MovieItem(modifier = Modifier
                .height(160.dp)
                .width(280.dp),
                item.backdropPath?.toPath() ?: "",
                favIds.contains(item.id)
            ) {
                onTapped()
            }
            MyText(
                modifier = Modifier
                    .width(250.dp)
                    .padding(horizontal = 12.dp),
                text = item.name ?: "",
                font = MyFont.Body14Italic,
                color = MyColors.indigoDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)

        }
        CircularProgressbar(
            number = (item.voteAverage?.toFloat()) * 10,
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

enum class PremiumPlan {
    OneMonth, TreeMonth, OneYear
}