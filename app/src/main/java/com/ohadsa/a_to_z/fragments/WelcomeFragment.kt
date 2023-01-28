package com.ohadsa.a_to_z.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.integrativit_client.MainViewModel
import com.ohadsa.a_to_z.ui.pages.WelcomePage
import com.ohadsa.a_to_z.R
import com.ohadsa.a_to_z.ui.generic.PopupCell
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.composeView).setContent {
            var withPopup by remember { mutableStateOf(false) }
            WelcomePage(viewModel = viewModel) {
                viewModel.login(
                    runWhenSuccess = {
                        findNavController().navigate(R.id.action_to_gameFragment)
                    },
                    runWhenFailure = {
                        withPopup = true
                    }
                )
            }
            if (withPopup) {
                PopupCell(
                    drawable = R.drawable.background_popup,
                    onBack = {
                        withPopup = false
                    },
                    onButton = {
                        viewModel.signUp()
                        findNavController().navigate(R.id.action_to_gameFragment)
                    },
                buttonText = stringResource(id = R.string.create),
                    text = stringResource(id = R.string.createText),
                    title = stringResource(id = R.string.userNotInData)
                )
            }
        }

    }
}