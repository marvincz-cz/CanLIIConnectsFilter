package cz.marvincz.canlii.settings

import com.russhwolf.settings.Settings
import cz.marvincz.canlii.Link
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class Storage {
    private val settings: Settings = SettingsProvider().settings

    fun getBlacklist(): List<Link> {
        val value = settings.getString(KEY_BLACKLIST, blacklistDefault)
        return Json.decodeFromString(value)
    }

    fun setBlacklist(blacklist: List<Link>) {
        val value = Json.encodeToString(blacklist)
        settings.putString(KEY_BLACKLIST, value)
    }

    private val blacklistDefault = Json.encodeToString(
        listOf(
            Link("Lexum Lab", "/en/publishers/216"),
            Link("Jurisage AI", "/en/publishers/13"),
        )
    )
}

private const val KEY_BLACKLIST = "BLACKLIST"

internal expect class SettingsProvider() {
    val settings: Settings
}