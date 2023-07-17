package cz.marvincz.canlii.parser

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import cz.marvincz.canlii.Link
import cz.marvincz.canlii.Summary
import cz.marvincz.canlii.SummaryField

class Parser {
    fun parse(html: String): Summary {
        val summary = Summary.Builder()
        val stack = mutableListOf<String?>()
        var link: Link.Builder? = null

        KsoupHtmlParser(object : KsoupHtmlHandler {
            override fun onOpenTag(
                name: String,
                attributes: Map<String, String>,
                isImplied: Boolean,
            ) {
                when (name) {
                    "div", "span" -> {
                        stack.add(attributes["class"])
                    }

                    "a" -> {
                        val node = stack.toStack()
                        if (node.endsWith("headline")) {
                            link =
                                Link.Builder(SummaryField.TITLE).apply { path = attributes["href"] }
                        } else if (node.endsWith("subtitle-wrapper")) {
                            link =
                                Link.Builder(SummaryField.CASE).apply { path = attributes["href"] }
                        } else if (node.endsWith("author.info.name.identifier")) {
                            val field = if (summary.author == null) SummaryField.AUTHOR else SummaryField.PUBLISHER
                            link = Link.Builder(field).apply { path = attributes["href"] }
                        }
                    }
                }
            }

            override fun onCloseTag(name: String, isImplied: Boolean) {
                when (name) {
                    "div", "span" -> stack.removeLast()
                    "a" -> link = null
                }
            }

            override fun onText(text: String) {
                link?.apply { title = text.trim() }?.build(summary)

                val node = stack.toStack()
                if (node.endsWith("stats")) {
                    concursRegex.matchEntire(text)?.destructured?.let { (concurs) ->
                        concurs.toIntOrNull()?.let {
                            summary.concurs = it
                        }
                    }
                    commentsRegex.matchEntire(text)?.destructured?.let { (comments) ->
                        comments.toIntOrNull()?.let {
                            summary.comments = it
                        }
                    }
                } else if (node.endsWith("date")) {
                    summary.date = text.trim()
                }
            }
        }).write(html.replace(clearRegex, ""))

        return summary.build()
    }
}

private fun List<String?>.toStack() = filterNotNull().joinToString(".")

// auto-closing tags were causing some crashes and we don't need them
private val clearRegex = Regex("</?\\s?(?:hr|br|if)\\s?/?>")
private val concursRegex = Regex("(\\d+) Concurs?")
private val commentsRegex = Regex("(\\d+) Comments?")