package com.ohadsa.a_to_z.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.integrativit_client.MainViewModel
import com.ohadsa.a_to_z.R
import com.ohadsa.a_to_z.ui.pages.GamePage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class GameFragment : Fragment(R.layout.fragment_game) {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeViewGame).setContent {
            GamePage(viewModel){
                findNavController().navigate(R.id.action_to_blank)
            }
        }

    }
}