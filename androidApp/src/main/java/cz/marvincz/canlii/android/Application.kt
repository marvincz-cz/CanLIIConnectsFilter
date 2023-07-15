package cz.marvincz.canlii.android

import android.app.Application
import cz.marvincz.canlii.koin.initKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {  }
    }
}