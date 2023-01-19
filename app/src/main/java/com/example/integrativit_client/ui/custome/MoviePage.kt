package com.example.integrativit_client.ui.custome


import androidx.compose.foundation.layout.Column
import com.example.integrativit_client.models.MovieResponse
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.integrativit_client.R
import com.example.integrativit_client.ui.generic.*
import com.example.integrativit_client.ui.theme.generic.MyText

const val BASE_BACKDROP_IMAGE_URL = "https://image.tmdb.org/t/p/w780/"
const val BASE_POSTER_IMAGE_URL = "https://image.tmdb.org/t/p/w500/"

@Composable
fun MoviePage(
    curItem: MovieResponse,
    isItemInFavorite :Boolean,
    isItemInWish :Boolean,
    onAddToFavoriteTapped: (MovieResponse) -> Unit,
    onAddToWishListTapped: (MovieResponse) -> Unit,
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.94f)
                .background(Color.White),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, false)
                    .fillMaxHeight(0.4F)
            ) {
                val (
                    backdropImage,
                    text,
                    movieTitleBox,
                    moviePosterImage,
                    translucentBr,
                ) = createRefs()

                ImageOrDefault(
                    imageUrl = "$BASE_BACKDROP_IMAGE_URL${curItem.objectDetails.backdrop_path}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .constrainAs(backdropImage) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                    height = 100.dp,
                    width = 100.dp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.White
                                ),
                                startY = 0.1F
                            )
                        )
                        .constrainAs(translucentBr) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(backdropImage.bottom)
                        }
                )

                Column(
                    modifier = Modifier.constrainAs(movieTitleBox) {
                        start.linkTo(moviePosterImage.end, margin = 12.dp)
                        end.linkTo(parent.end, margin = 12.dp)
                        bottom.linkTo(moviePosterImage.bottom, margin = 10.dp)
                    },
                    verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Movie",
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(size = 4.dp))
                                .background(Color.DarkGray.copy(alpha = 0.65F))
                                .padding(2.dp),
                            color = Color.Black.copy(alpha = 0.78F),
                            fontSize = 12.sp,
                        )
                        Text(
                            text = "PG",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clip(shape = RoundedCornerShape(size = 4.dp))
                                .background(
                                    Color.DarkGray.copy(alpha = 0.65F)
                                )
                                .padding(2.dp),
                            color = Color.Black.copy(alpha = 0.78F),
                            fontSize = 12.sp,
                        )
                    }

                    Text(
                        text = curItem.objectDetails.title,
                        modifier = Modifier
                            .padding(top = 2.dp, start = 4.dp, bottom = 4.dp)
                            .fillMaxWidth(0.5F),
                        maxLines = 2,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black.copy(alpha = 0.78F)
                    )

                    Text(
                        text = curItem.objectDetails.release_date,
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Black.copy(alpha = 0.56F)
                    )

                    CircularProgressbar(
                        number = curItem.objectDetails.vote_average.toFloat() * 10,
                        size = 28.dp,
                        indicatorThickness = 6.dp,
                        modifier = Modifier
                            .padding(6.dp))

                }

                ImageOrDefault(
                    imageUrl = "$BASE_POSTER_IMAGE_URL/${curItem.objectDetails.poster_path}",
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .width(115.dp)
                        .height(172.5.dp)
                        .constrainAs(moviePosterImage) {
                            top.linkTo(backdropImage.bottom)
                            bottom.linkTo(backdropImage.bottom)
                            start.linkTo(parent.start)
                        },
                    height = 100.dp,
                    width = 100.dp
                )
                Column(modifier = Modifier
                    .padding(top = 3.dp, bottom = 4.dp, start = 12.dp, end = 4.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .constrainAs(text) {
                        top.linkTo(moviePosterImage.bottom, 24.dp)
                        start.linkTo(parent.start, 12.dp)
                        end.linkTo(parent.end, 12.dp)
                    }
                ) {
                    MyText(
                        text = curItem.objectDetails.overview,
                        overflow = TextOverflow.Ellipsis,
                        font = MyFont(weight = FontWeight.Normal,
                            textSize = 18.sp,
                            fontStyle = FontStyle.Italic),
                        color = Color.Black.copy(alpha = 0.75F))
                }

            }
            Spacer(modifier = Modifier.height(36.dp))
            Row(
                Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ButtonV2(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = if (isItemInFavorite) "Remove From Favorite" else "Add To Favorite",
                    variant = if (isItemInFavorite) ButtonVariant.Secondary else ButtonVariant.Primary,
                    onClick = { onAddToFavoriteTapped(curItem) })


                Spacer(modifier = Modifier.width(4.dp))
                ButtonV2(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = if (isItemInWish) "Remove From WishList" else "Add To WishList",
                    variant = if (isItemInWish) ButtonVariant.Secondary else ButtonVariant.Primary,
                    onClick = { onAddToWishListTapped(curItem) })
            }
        }
        if (curItem.objectDetails.isFavorite) {
            DrawableImage(
                modifier = Modifier
                    .padding(24.dp)
                    .size(40.dp)
                    .align(Alignment.TopEnd),
                id = R.drawable.favorite_heart_,
                svg = false)
        }
    }
}