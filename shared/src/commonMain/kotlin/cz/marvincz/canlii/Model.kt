package cz.marvincz.canlii

import kotlinx.serialization.Serializable

@Serializable
internal data class Page(
    val results: List<String>,
    val hasMoreResults: Boolean,
)

data class PageResult(
    val items: List<Summary>,
    val hasMoreResults: Boolean,
    val nextPage: Int,
)

data class Summary(
    val title: Link,
    val case: Link,
    val author: Link,
    val publisher: Link,
    val concurs: Int,
    val date: String,
) {
    internal class Builder {
        var title: Link? = null
        var case: Link? = null
        var author: Link? = null
        var publisher: Link? = null
        var concurs: Int = 0
        var date: String? = null

        fun build() = Summary(
            title = requireNotNull(title),
            case = requireNotNull(case),
            author = requireNotNull(author),
            publisher = requireNotNull(publisher ?: author),
            concurs = concurs,
            date = requireNotNull(date),
        )
    }
}

@Serializable
data class Link(
    val title: String,
    val path: String,
) {
    internal class Builder(private val field: SummaryField? = null) {
        var title: String? = null
        var path: String? = null

        private fun build() = Link(
            title = requireNotNull(title),
            path = requireNotNull(path),
        )

        fun build(summary: Summary.Builder) {
            when (field) {
                SummaryField.TITLE -> summary.title = build()
                SummaryField.CASE -> summary.case = build()
                SummaryField.AUTHOR -> summary.author = build()
                SummaryField.PUBLISHER -> summary.publisher = build()
                else -> {}
            }
        }
    }
}

internal enum class SummaryField {
    TITLE, CASE, AUTHOR, PUBLISHER, CONCURS, DATE
}