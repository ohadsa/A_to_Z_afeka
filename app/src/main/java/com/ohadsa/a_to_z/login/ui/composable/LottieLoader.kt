package com.ohadsa.a_to_z.login.ui.composable

import androidx.annotation.RawRes
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.*

@Composable
fun LottieLoader(
    modifier: Modifier = Modifier,
    id: Int,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(id))
    val progress by animateLottieCompositionAsState(composition,
        iterations = LottieConstants.IterateForever)
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { progress },
    )
}