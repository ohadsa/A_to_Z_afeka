package com.example.integrativit_client.ui.custome


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.integrativit_client.ui.generic.ImageOrDefault
import com.example.integrativit_client.ui.generic.clickableNoFeedback
import com.example.integrativit_client.R
import com.example.integrativit_client.ui.generic.DrawableImage

@Composable
fun MovieItem(
    modifier: Modifier,
    imageUrl: String,
    isFav: Boolean,
    big: Boolean = false,
    onClick: () -> Unit,
) {
    Box {
        Card(modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))) {
            ImageOrDefault(
                imageUrl = imageUrl.ifEmpty { null },
                width = 100.dp,
                height = 100.dp,
                modifier = Modifier.clickableNoFeedback { onClick() },
                defaultValue = R.drawable.back,
                contentScale = ContentScale.Crop
            )
        }
        if (isFav) {
            DrawableImage(
                modifier = Modifier
                    .padding(16.dp)
                    .size(if (big) 20.dp else 12.dp)
                    .align(Alignment.TopEnd),
                id = R.drawable.favorite_heart_,
                svg = false)
        }
    }
}