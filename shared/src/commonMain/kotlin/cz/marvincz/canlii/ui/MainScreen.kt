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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cz.marvincz.canlii.AppComponent
import cz.marvincz.canlii.MR
import cz.marvincz.canlii.Summary
import cz.marvincz.canlii.toUrl
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun MainScreen(
    uiState: AppComponent.UiState,
    items: List<Summary>,
    onMore: () -> Unit,
) {
    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(stringResource(MR.strings.title)) })
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
                        items(items = list) {
                            Item(it)
                        }
                    }
                    if (uiState == AppComponent.UiState.Loading) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    if (uiState is AppComponent.UiState.Success && uiState.hasMoreResults) {
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
            }
        )
    }
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
internal fun Item(summary: Summary) {
    val uriHandler = LocalUriHandler.current
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { uriHandler.openUri(summary.title.toUrl()) },
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
                modifier = Modifier.clickable { uriHandler.openUri(summary.case.toUrl()) },
                text = summary.case.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            AuthorText(summary)

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(MR.plurals.concurs, summary.concurs, summary.concurs),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

private fun groupByDate(data: List<Summary>): List<Pair<String, List<Summary>>> {
    return data.groupBy { it.date }.map { it.toPair() }
}