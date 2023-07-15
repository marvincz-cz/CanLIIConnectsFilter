package cz.marvincz.canlii.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import cz.marvincz.canlii.DefaultAppComponent
import cz.marvincz.canlii.koin.initKoin
import cz.marvincz.canlii.ui.App

@OptIn(ExperimentalDecomposeApi::class)
fun main() {
    initKoin {}

    val lifecycle = LifecycleRegistry()
    val stateKeeper = StateKeeperDispatcher()

    val component = DefaultAppComponent(
        componentContext = DefaultComponentContext(
            lifecycle = lifecycle,
            stateKeeper = stateKeeper,
        ),
    )
    application {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)

        Window(onCloseRequest = ::exitApplication) {
            App(component = component)
        }
    }
}