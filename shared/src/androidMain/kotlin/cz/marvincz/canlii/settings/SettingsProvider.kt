package cz.marvincz.canlii.settings

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal actual class SettingsProvider : KoinComponent {
    private val context: Context = get()

    actual val settings: Settings
        get() = SharedPreferencesSettings(
            context.getSharedPreferences(
                "canlii_connects_filter",
                Context.MODE_PRIVATE
            )
        )
}