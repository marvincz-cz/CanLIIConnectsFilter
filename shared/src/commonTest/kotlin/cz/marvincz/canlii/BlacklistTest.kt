package cz.marvincz.canlii

import kotlin.test.Test
import kotlin.test.assertContentEquals

class BlacklistTest {
    private val baseSummary = Summary(
        title = Link("", ""),
        case = Link("", ""),
        author = Link("", ""),
        publisher = Link("", ""),
        concurs = 0,
        date = "",
    )

    private val link = Link(title = "Link", path = "/en/users/11631")

    private val blacklist = listOf(link)

    @Test
    fun `block publisher`() {
        val list = listOf(baseSummary.copy(publisher = link))

        assertBlacklistFiltering(list, emptyList())
    }

    @Test
    fun `not block author`() {
        val list = listOf(baseSummary.copy(author = link))

        assertBlacklistFiltering(list, list)
    }

    @Test
    fun `block with different title`() {
        val list = listOf(baseSummary.copy(publisher = link.copy(title = "DIFFERENT")))

        assertBlacklistFiltering(list, emptyList())
    }

    private fun assertBlacklistFiltering(list: List<Summary>, expected: List<Summary>) {
        val filtered = list.filteredByBlacklist(blacklist)

        assertContentEquals(expected, filtered)
    }
}