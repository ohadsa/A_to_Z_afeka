package com.example.integrativit_client.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import com.example.integrativit_client.MainViewModel
import com.example.integrativit_client.R
import com.example.integrativit_client.ui.pages.GamePage


class GameFragment : Fragment(R.layout.fragment_game) {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeViewGame).setContent {
            GamePage()
        }

    }
}