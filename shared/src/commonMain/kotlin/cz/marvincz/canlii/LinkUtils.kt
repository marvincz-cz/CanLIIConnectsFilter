package cz.marvincz.canlii

import androidx.compose.ui.platform.UriHandler

const val BASE_URL = "https://canliiconnects.org"
const val SEARCH_PATH = "ajax/en/"

fun UriHandler.open(link: Link) = openUri(link.toUrl())