package cz.marvincz.canlii.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import cz.marvincz.canlii.MR
import cz.marvincz.canlii.component.DefaultAppComponent
import cz.marvincz.canlii.koin.initKoin
import cz.marvincz.canlii.ui.App
import dev.icerock.moko.resources.compose.stringResource

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

        Window(onCloseRequest = ::exitApplication, title = stringResource(MR.strings.title)) {
            App(component = component)
        }
    }
}