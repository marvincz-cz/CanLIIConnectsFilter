package cz.marvincz.canlii.settings

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

internal actual class SettingsProvider {
    actual val settings: Settings
        get() = PreferencesSettings(Preferences.userNodeForPackage(SettingsProvider::class.java))
}