package cz.marvincz.canlii.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import cz.marvincz.canlii.MR
import cz.marvincz.canlii.Summary
import cz.marvincz.canlii.component.MainComponent
import cz.marvincz.canlii.open
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun MainRoute(component: MainComponent) {
    val uiState by component.uiState.subscribeAsState()
    val summaries by component.summaries.subscribeAsState()

    MainScreen(
        uiState = uiState,
        items = summaries,
        onMore = component::onLoad,
        onReload = component::onReload,
        onBlock = component::onBlock,
        onNavigateToBlacklist = component::onNavigateToBlacklist,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun MainScreen(
    uiState: MainComponent.UiState,
    items: List<Summary>,
    onMore: () -> Unit,
    onReload: () -> Unit,
    onBlock: (Summary) -> Unit,
    onNavigateToBlacklist: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(MR.strings.title)) },
                actions = {
                    TextButton(
                        onClick = onNavigateToBlacklist,
                        colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current)
                    ) { Text(stringResource(MR.strings.blacklist).uppercase()) }
                    IconButton(
                        onClick = onReload,
                        enabled = uiState is MainComponent.UiState.Success,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(MR.strings.button_reload),
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            val itemsWithHeaders = remember(items) { groupByDate(items) }

            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                itemsWithHeaders.forEach { (dateHeader, list) ->
                    stickyHeader(key = dateHeader) {
                        HeaderItem(dateHeader)
                    }
                    items(
                        items = list,
                        key = { it.title.toString() },
                    ) { Item(it, onBlock) }
                }
                if (uiState == MainComponent.UiState.Loading) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                if (uiState is MainComponent.UiState.Success && uiState.hasMoreResults) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            FilledTonalButton(
                                onClick = onMore,
                            ) {
                                Text(stringResource(MR.strings.button_load))
                            }
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun HeaderItem(header: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 8.dp),
    ) {
        Text(text = header, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
internal fun Item(summary: Summary, onBlock: (Summary) -> Unit) {
    val uriHandler = LocalUriHandler.current
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { uriHandler.open(summary.title) },
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = summary.title.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                modifier = Modifier.clickable { uriHandler.open(summary.case) },
                text = summary.case.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    AuthorText(summary)

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(MR.plurals.concurs, summary.concurs, summary.concurs),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                IconButton(
                    modifier = Modifier.align(Alignment.Bottom),
                    onClick = { onBlock(summary) },
                ) {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = stringResource(
                            MR.strings.button_block,
                            summary.publisher.title
                        )
                    )
                }
            }
        }
    }
}

private fun groupByDate(data: List<Summary>): List<Pair<String, List<Summary>>> {
    return data.groupBy { it.date }.map { it.toPair() }
}