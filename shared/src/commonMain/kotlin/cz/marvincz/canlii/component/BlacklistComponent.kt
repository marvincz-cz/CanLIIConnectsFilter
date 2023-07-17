package cz.marvincz.canlii.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import cz.marvincz.canlii.Link
import cz.marvincz.canlii.settings.Storage
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

interface BlacklistComponent {
    val blacklist: Value<List<Link>>

    fun onRemove(link: Link)
    fun onBack()
}

class DefaultBlacklistComponent(componentContext: ComponentContext, private val back: () -> Unit) :
    BlacklistComponent, KoinComponent, ComponentContext by componentContext {
    private val storage: Storage = get()

    private val _blacklist = MutableValue(storage.getBlacklist())
    override val blacklist: Value<List<Link>> = _blacklist

    override fun onRemove(link: Link) {
        val newBlacklist = storage.getBlacklist() - link
        storage.setBlacklist(newBlacklist)
        _blacklist.value = newBlacklist
    }

    override fun onBack() = back()
}