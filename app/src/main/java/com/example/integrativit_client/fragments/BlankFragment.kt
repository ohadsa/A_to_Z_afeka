package com.example.integrativit_client.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import com.example.integrativit_client.MainViewModel
import com.example.integrativit_client.R
import com.example.integrativit_client.ui.pages.GamePage
import com.example.integrativit_client.ui.pages.ListPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class BlankFragment : Fragment(R.layout.fragment_blank) {


    private val viewModel: MainViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeView).setContent {
            ListPage(viewModel){
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }


}

