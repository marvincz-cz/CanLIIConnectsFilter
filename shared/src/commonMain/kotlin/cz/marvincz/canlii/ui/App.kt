package cz.marvincz.canlii.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import cz.marvincz.canlii.AppComponent

@Composable
fun App(component: AppComponent) {
    val uiState by component.uiState.subscribeAsState()
    val summaries by component.summaries.subscribeAsState()

    MainScreen(
        uiState = uiState,
        items = summaries,
        onMore = component::onLoad,
    )
}