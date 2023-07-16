package cz.marvincz.canlii

import io.ktor.http.URLBuilder
import io.ktor.http.encodedPath

const val BASE_URL = "https://canliiconnects.org"
const val SEARCH_PATH = "ajax/en/"

fun Link.toUrl() = URLBuilder(BASE_URL).apply {
    val urlPart = URLBuilder(path).build()

    encodedPath = urlPart.encodedPath
    parameters.appendAll(urlPart.parameters)
}.buildString()