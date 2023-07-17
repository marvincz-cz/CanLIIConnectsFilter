package cz.marvincz.canlii.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

@OptIn(ExperimentalTextApi::class)
@Composable
internal fun AuthorText(summary: Summary) {
    val uriHandler = LocalUriHandler.current

    val textStyle = MaterialTheme.typography.bodyLarge
    val textColor = LocalContentColor.current
    val linkColor = MaterialTheme.colorScheme.primary
    val authorString = remember(summary, MaterialTheme.colorScheme) {
        authorString(
            summary = summary,
            textStyle = textStyle,
            color = textColor,
            linkColor = linkColor
        )
    }

    ClickableText(
        modifier = Modifier.padding(start = 16.dp),
        text = authorString,
    ) { position ->
        authorString.getUrlAnnotations(position, position).firstOrNull()?.let {
            uriHandler.openUri(it.item.url)
        }
    }
}

private fun authorString(
    summary: Summary,
    textStyle: TextStyle,
    color: Color,
    linkColor: Color,
) = buildAnnotatedString {
    append("by ".annotatedWithStyle(textStyle, color))
    appendClickableText(summary.author, textStyle, linkColor)
    if (summary.publisher != summary.author) {
        append(" — ".annotatedWithStyle(textStyle, color))
        appendClickableText(summary.publisher, textStyle, linkColor)
    }
}

@OptIn(ExperimentalTextApi::class)
private fun AnnotatedString.Builder.appendClickableText(
    link: Link,
    textStyle: TextStyle,
    color: Color,
) {
    withAnnotation(UrlAnnotation(link.toUrl())) {
        append(link.title.annotatedWithStyle(textStyle, color))
    }
}

private fun String.annotatedWithStyle(textStyle: TextStyle, color: Color) =
    AnnotatedString(this, textStyle.toSpanStyle().copy(color = color))