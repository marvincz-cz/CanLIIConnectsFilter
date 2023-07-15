package cz.marvincz.canlii.koin

import cz.marvincz.canlii.ktor.Client
import cz.marvincz.canlii.parser.Parser
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration) {
    startKoin {
        modules(commonModule(), )
        appDeclaration()
    }
}

private fun commonModule() = module {
    singleOf(::Parser)
    singleOf(::Client)
}