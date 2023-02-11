package com.ohadsa.a_to_z.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import com.ohadsa.a_to_z.MainViewModel
import com.ohadsa.a_to_z.ui.pages.ListPage
import com.ohadsa.a_to_z.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class ListFragment : Fragment(R.layout.fragment_list) {


    private val viewModel: MainViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeView).setContent {
            val premium by viewModel.isPremium.collectAsState()
            ListPage(
                modifier = Modifier ,
                viewModel = viewModel){
                println("onBackPressed")
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }


}

