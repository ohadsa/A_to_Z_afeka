package com.ohadsa.a_to_z.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import com.example.integrativit_client.MainViewModel
import com.ohadsa.a_to_z.ui.pages.ListPage
import com.ohadsa.a_to_z.R
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

