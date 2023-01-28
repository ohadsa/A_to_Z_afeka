package com.ohadsa.a_to_z.ui.generic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ohadsa.a_to_z.ui.theme.MyColors
import com.ohadsa.a_to_z.R
@Composable
fun PopupCell(
    drawable :Int,
    onButton: () -> Unit,
    buttonText:String,
    title:String,
    text:String,
    onBack: () -> Unit,
) {
    Box(Modifier
        .clickableNoFeedback { onBack() }
        .fillMaxSize()
        .background(Color.Transparent.copy(alpha = 0.5f))) {
        Box(Modifier
            .width(335.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White,RoundedCornerShape(8.dp))
            .align(Alignment.Center)) {
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MyColors.indigoDark,
                                    Color.White,
                                )
                            )
                        )
                )
                MyText(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = title ,
                    font = MyFont.Heading5,
                    lineHeight = MyFont.Heading5.lineHeight,
                    color = MyColors.darkGray)
                Spacer(Modifier.height(8.dp))

                MyText(
                    modifier = Modifier.padding(horizontal = 36.dp),
                    text = text,
                    font = MyFont.Body14,
                    lineHeight = MyFont.Body14.lineHeight,
                    color = MyColors.gray50)
                Spacer(Modifier.height(30.dp))
                ButtonV2(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    variant = ButtonVariant.Primary,
                    text = buttonText,
                    onClick = { onButton() }
                )
                Spacer(Modifier.height(8.dp))
                ButtonV2(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    variant = ButtonVariant.Secondary,
                    text = stringResource(id = R.string.edit),
                    onClick = { onBack() }
                )
                Spacer(Modifier.height(36.dp))
            }
        }
    }
}
