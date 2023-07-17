package cz.marvincz.canlii.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.defaultComponentContext
import cz.marvincz.canlii.component.DefaultAppComponent
import cz.marvincz.canlii.ui.App

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = DefaultAppComponent(componentContext = defaultComponentContext())

        setContent {
            App(component = component)
        }
    }
}