package cz.marvincz.canlii

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import cz.marvincz.canlii.ktor.Client
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

interface AppComponent {
    val uiState: Value<UiState>
    val summaries: Value<List<Summary>>

    sealed interface UiState {
        object Loading : UiState
        data class Success(val hasMoreResults: Boolean) : UiState
        object Error : UiState
    }

    fun onLoad()
}

class DefaultAppComponent(componentContext: ComponentContext) : AppComponent, KoinComponent,
    ComponentContext by componentContext {
    private val client: Client = get()
    private val coroutineScope = coroutineScope()
    private var page = 1

    private val channel = Channel<AppComponent.UiState>()
    override val uiState: Value<AppComponent.UiState> = flow {
        for (uiState in channel) {
            emit(uiState)
        }
    }.asValue(initialValue = AppComponent.UiState.Loading, lifecycle = lifecycle)

    private val _summaries = MutableValue(emptyList<Summary>())
    override val summaries: Value<List<Summary>> = _summaries

    init {
        onLoad()
    }

    override fun onLoad() {
        coroutineScope.launch {
            channel.send(AppComponent.UiState.Loading)

            val (newItems, hasMoreResults, nextPage) = try {
                client.getPageFiltered(page, blacklist)
            } catch (e: Exception) {
                channel.send(AppComponent.UiState.Error)
                return@launch
            }

            page = nextPage
            _summaries.value = _summaries.value + newItems
            channel.send(AppComponent.UiState.Success(hasMoreResults))
        }
    }

    private val blacklist = listOf(
        "/en/publishers/216",
        "/en/publishers/13",
    )
}