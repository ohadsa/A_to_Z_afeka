package com.example.integrativit_client.ui.generic

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.material.placeholder
import com.example.integrativit_client.R
import com.example.integrativit_client.ui.theme.MyColors
import com.example.integrativit_client.ui.theme.generic.MyText

@Composable
fun TextWithArrow(
    text: String,
    modifier: Modifier = Modifier,
    font: MyFont = MyFont.Body14,
    color: Color = Color.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    placeHolder: Boolean = false,
    enableArrow: Boolean = true,
    leadingIcon : Int? = null,
    drawable: Int = R.drawable.left_arrow,
    firstText: Boolean = true,
    onClick: () -> Unit = {},
    ) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .placeholder(
                shape = RoundedCornerShape(20.dp),
                visible = placeHolder,
                color = MyColors.gray10,
                highlight = PlaceholderHighlight.fade(highlightColor = MyColors.gray5))
            .clickableNoFeedback(interactionSource) {
                onClick()
            }
            .background(
                if (isPressed) MyColors.gray15 else Color.Transparent,
                RoundedCornerShape(25.dp))
    ) {
        leadingIcon?.let {
            DrawableImage(
                id = it,
                modifier = Modifier
                    .padding(bottom = 10.dp, start = 12.dp , end = 8.dp)
                    .size(24.dp),
                contentScale = ContentScale.Crop,
                svg = true
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        if (firstText) {
            MyText(
                text = text,
                modifier = Modifier,
                font = font,
                color = color,
                textAlign = textAlign,
                lineHeight = lineHeight,
                maxLines = maxLines
            )
            if (enableArrow) {
                DrawableImage(
                    id = drawable,
                    modifier = Modifier
                        .padding(bottom = 2.dp, start = 8.dp)
                        .size(22.dp),
                    contentScale = ContentScale.Crop,
                    svg = true
                )
            }
        } else {
            if (enableArrow) {
                Box(Modifier.height(18.dp)) {
                    DrawableImage(
                        id = drawable,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(end = 6.dp)
                            .height(14.dp)
                            .width(14.dp),
                        contentScale = ContentScale.Crop,
                        svg = true
                    )
                }
            }
            MyText(
                text = text,
                modifier = Modifier,
                font = font,
                color = color,
                textAlign = textAlign,
                lineHeight = lineHeight,
                maxLines = maxLines
            )
        }
    }
}

@Preview
@Composable
fun PreviewTextWithArrow() {
    TextWithArrow(text = "Shop I follow", leadingIcon = R.drawable.fire)
}