package cz.marvincz.canlii.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import cz.marvincz.canlii.Link
import cz.marvincz.canlii.MR
import cz.marvincz.canlii.component.BlacklistComponent
import cz.marvincz.canlii.open
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun BlacklistRoute(component: BlacklistComponent) {
    val blacklist by component.blacklist.subscribeAsState()

    BlacklistScreen(
        blacklist = blacklist,
        onRemove = component::onRemove,
        onBack = component::onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BlacklistScreen(
    blacklist: List<Link>,
    onRemove: (Link) -> Unit,
    onBack: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(MR.strings.blacklist)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(MR.strings.button_back),
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                items(blacklist, key = { it.path }) { link ->
                    ListItem(
                        modifier = Modifier.clickable { uriHandler.open(link) },
                        headlineText = { Text(link.title) },
                        supportingText = { Text(link.path) },
                        trailingContent = {
                            IconButton(onClick = { onRemove(link) }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = stringResource(MR.strings.blacklist_remove),
                                )
                            }
                        },
                    )
                }
            }
        },
    )
}