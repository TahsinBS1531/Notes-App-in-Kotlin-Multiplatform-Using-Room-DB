package com.jetbrains.notes.data.di

import com.jetbrains.notes.data.repository.BaseLocalRepository
import com.jetbrains.notes.data.repository.LocalRepositoryImpl
import com.jetbrains.notes.ui.home.HomeViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module


fun initKoin(
    additionalModule: List<Module> = emptyList(),
    appDeclaration: KoinAppDeclaration = {}
) = startKoin {
    appDeclaration()
    modules(CommonModules(), PlatformModule())

}

fun CommonModules() = module {
    singleOf(::LocalRepositoryImpl).bind<BaseLocalRepository>()
//    viewModelOf(::HomeViewModel)
}

fun PlatformModule() = module {

}
//
//fun NetworkModule() = module {
//    single {
//        HttpClient {
//            install(HttpTimeout) {
//                val timeout = 30000L
//                connectTimeoutMillis = timeout
//                requestTimeoutMillis = timeout
//                socketTimeoutMillis = timeout
//            }
//            install(ContentNegotiation) {
//                json(Json {
//                    prettyPrint = true
//                    ignoreUnknownKeys = true
//                })
//            }
//        }
//    }
//}

