package cz.marvincz.canlii.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.unit.dp
import cz.marvincz.canlii.Link
import cz.marvincz.canlii.Summary
import cz.marvincz.canlii.toUrl

@OptIn(ExperimentalTextApi::class)
@Composable
fun AuthorText(summary: Summary) {
    val uriHandler = LocalUriHandler.current
    val authorString = authorString(summary)

    ClickableText(
        modifier = Modifier.padding(start = 16.dp),
        text = authorString,
    ) { position ->
        authorString.getUrlAnnotations(position, position).firstOrNull()?.let {
            uriHandler.openUri(it.item.url)
        }
    }
}

@Composable
private fun authorString(summary: Summary) = buildAnnotatedString {
    append("by ")
    appendClickableText(summary.author, MaterialTheme.typography.bodyLarge)
    if (summary.publisher != summary.author) {
        append(" — ")
        appendClickableText(summary.publisher, MaterialTheme.typography.bodyLarge)
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun AnnotatedString.Builder.appendClickableText(
    link: Link,
    textStyle: TextStyle,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    withAnnotation(UrlAnnotation(link.toUrl())) {
        append(AnnotatedString(link.title, textStyle.toSpanStyle().copy(color = color)))
    }
}