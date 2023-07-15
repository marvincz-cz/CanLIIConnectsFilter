package cz.marvincz.canlii.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import cz.marvincz.canlii.AppComponent
import cz.marvincz.canlii.Summary
import cz.marvincz.canlii.toUrl

@Composable
fun App(component: AppComponent) {
    val uiState by component.uiState.subscribeAsState()
    val summaries by component.summaries.subscribeAsState()

    Screen(
        uiState = uiState,
        items = summaries,
        onMore = component::onLoad,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(
    uiState: AppComponent.UiState,
    items: List<Summary>,
    onMore: () -> Unit,
) {
    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("CanLII Connects Filtered") })
            },
            content = { paddingValues ->
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(items = items) {
                        Item(it)
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
                                    Text("Load more")
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
fun Item(summary: Summary) {
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
            Text(summary.title.title, style = MaterialTheme.typography.headlineMedium)
            Text(
                modifier = Modifier.clickable { uriHandler.openUri(summary.case.toUrl()) },
                text = summary.case.title,
                style = MaterialTheme.typography.titleLarge,
            )

            AuthorText(summary)

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = summary.date,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}