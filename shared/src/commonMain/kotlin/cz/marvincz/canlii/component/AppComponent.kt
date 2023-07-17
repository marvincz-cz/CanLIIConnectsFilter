package cz.marvincz.canlii.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import org.koin.core.component.KoinComponent

interface AppComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child
    data class Main(val component: MainComponent) : Child
    data class Blacklist(val component: BlacklistComponent) : Child
}

class DefaultAppComponent(componentContext: ComponentContext) : AppComponent, KoinComponent,
    ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val _stack = childStack(
        source = navigation,
        initialConfiguration = Config.Main,
        handleBackButton = true,
        childFactory = ::child,
    )

    override val stack: Value<ChildStack<*, AppComponent.Child>> = _stack

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): AppComponent.Child =
        when (config) {
            Config.Main -> AppComponent.Main(
                DefaultMainComponent(
                    componentContext = componentContext,
                    navigateToBlacklist = { navigation.push(Config.Blacklist) }
                ),
            )

            Config.Blacklist -> AppComponent.Blacklist(
                DefaultBlacklistComponent(
                    componentContext = componentContext,
                    back = { navigation.pop() },
                ),
            )
        }


    @Parcelize
    private sealed interface Config : Parcelable {
        object Main : Config
        object Blacklist : Config
    }
}