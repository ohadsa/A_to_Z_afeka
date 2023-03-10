package com.ohadsa.a_to_z.ui.generic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*

@Composable
fun LottieLoader(
    modifier: Modifier = Modifier,
    id: Int,
    iterations: Int? = null,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(id))
    val progress by animateLottieCompositionAsState(composition, restartOnPlay = false)
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { progress },
    )
}