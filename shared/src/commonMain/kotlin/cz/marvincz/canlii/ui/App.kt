package cz.marvincz.canlii.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import cz.marvincz.canlii.component.AppComponent

@Composable
fun App(component: AppComponent) {
    AppTheme {
        Children(
            stack = component.stack,
            animation = stackAnimation(slide()),
        ) {
            when (val child = it.instance) {
                is AppComponent.Blacklist -> BlacklistRoute(child.component)
                is AppComponent.Main -> MainRoute(child.component)
            }
        }
    }
}