package com.ohadsa.a_to_z.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import com.ohadsa.a_to_z.MainViewModel
import com.ohadsa.a_to_z.R
import com.ohadsa.a_to_z.ui.pages.ListPage
import com.ohadsa.a_to_z.ui.pages.SearchPage


class SearchFragment :  Fragment(R.layout.fragment_search) {

    private val viewModel: MainViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeViewSearch).setContent {
            SearchPage(
                modifier = Modifier,
                viewModel){
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }
}
