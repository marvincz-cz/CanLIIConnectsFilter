package cz.marvincz.canlii

import kotlin.test.Test
import kotlin.test.assertEquals

class LinkTest {
    @Test
    fun `summary URL`() {
        testUrl(
            path = "/en/summaries/91844",
            url = "https://canliiconnects.org/en/summaries/91844"
        )
    }
    @Test
    fun `author URL`() {
        testUrl(
            path = "/en/users/11631",
            url = "https://canliiconnects.org/en/users/11631",
        )
    }

    @Test
    fun `author URL with search and spaces`() {
        testUrl(
            path = "/en/search?authorOrPublisher=James (Jim) D. Fraser, Thomas D. Boyd",
            url = "https://canliiconnects.org/en/search?authorOrPublisher=James+%28Jim%29+D.+Fraser%2C+Thomas+D.+Boyd",
            // Actual CanLII url = "https://canliiconnects.org/en/search?authorOrPublisher=James%20(Jim)%20D.%20Fraser,%20Thomas%20D.%20Boyd",
        )
    }

    private fun testUrl(path: String, url: String) {
        val link = Link("Link", path)

        assertEquals(expected = url, actual = link.toUrl())
    }
}