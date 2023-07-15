package cz.marvincz.canlii.ktor

import cz.marvincz.canlii.BASE_URL
import cz.marvincz.canlii.Page
import cz.marvincz.canlii.PageResult
import cz.marvincz.canlii.parser.Parser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class Client : KoinComponent {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
    private val parser: Parser = get()

    suspend fun getPage(page: Int): PageResult {
        val response = client.get(BASE_URL) {
            url {
                path("ajax/en/")
            }
            parameter("page", page.coerceAtLeast(1))
        }
        val body: Page = response.body()

        return PageResult(body.results.map(parser::parse), body.hasMoreResults)
    }
}