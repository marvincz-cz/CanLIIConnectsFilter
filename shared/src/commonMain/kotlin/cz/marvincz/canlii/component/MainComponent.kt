package cz.marvincz.canlii.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import cz.marvincz.canlii.Summary
import cz.marvincz.canlii.asValue
import cz.marvincz.canlii.coroutineScope
import cz.marvincz.canlii.filteredByBlacklist
import cz.marvincz.canlii.ktor.Client
import cz.marvincz.canlii.settings.Storage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

interface MainComponent {
    val uiState: Value<UiState>
    val summaries: Value<List<Summary>>

    fun onLoad()
    fun onReload()
    fun onBlock(summary: Summary)
    fun onNavigateToBlacklist()

    sealed interface UiState {
        object Loading : UiState
        data class Success(val hasMoreResults: Boolean) : UiState
        object Error : UiState
    }
}

internal class DefaultMainComponent(
    componentContext: ComponentContext,
    private val navigateToBlacklist: () -> Unit,
) : MainComponent, KoinComponent,
    ComponentContext by componentContext {
    private val client: Client = get()
    private val storage: Storage = get()
    private val coroutineScope = coroutineScope()

    private val channel = Channel<MainComponent.UiState>()
    override val uiState: Value<MainComponent.UiState> = flow {
        for (uiState in channel) {
            emit(uiState)
        }
    }.asValue(initialValue = MainComponent.UiState.Loading, lifecycle = lifecycle)

    private var page = 1
    private val _summaries = MutableValue(emptyList<Summary>())
    override val summaries: Value<List<Summary>> = _summaries

    init {
        onLoad()
    }

    override fun onLoad() {
        coroutineScope.launch {
            channel.send(MainComponent.UiState.Loading)

            val blacklist = storage.getBlacklist()

            val (newItems, hasMoreResults, nextPage) = try {
                client.getPageFiltered(page, blacklist)
            } catch (e: Exception) {
                channel.send(MainComponent.UiState.Error)
                return@launch
            }

            page = nextPage
            _summaries.value = _summaries.value + newItems
            channel.send(MainComponent.UiState.Success(hasMoreResults))
        }
    }

    override fun onReload() {
        page = 1
        _summaries.value = emptyList()

        onLoad()
    }

    override fun onBlock(summary: Summary) {
        coroutineScope.launch {
            val blacklist = storage.getBlacklist()
            // just in case - this shouldn't happen, such item should already be blocked from the list
            if (blacklist.any { it.path == summary.publisher.path }) return@launch

            val newBlacklist = blacklist + summary.publisher
            storage.setBlacklist(newBlacklist)

            _summaries.value = _summaries.value.filteredByBlacklist(newBlacklist)
        }
    }

    override fun onNavigateToBlacklist() = navigateToBlacklist()
}