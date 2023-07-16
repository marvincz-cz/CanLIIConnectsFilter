package cz.marvincz.canlii.android

import android.app.Application
import cz.marvincz.canlii.koin.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            modules(module { androidContext(this@Application) })
        }
    }
}