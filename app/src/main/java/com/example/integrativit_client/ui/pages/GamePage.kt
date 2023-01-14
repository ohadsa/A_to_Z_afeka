package com.example.integrativit_client.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.integrativit_client.ui.generic.MyFont
import com.example.integrativit_client.ui.theme.MyColors
import com.example.integrativit_client.ui.theme.generic.MyText

@Composable
fun GamePage(
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        MyText(
            text = "that page will be home page with movies ",
            font = MyFont.Heading5,
            color = MyColors.main,
            lineHeight = MyFont.Heading5.lineHeight)
    }
}