package com.jetbrains.notes.data.di

import com.jetbrains.notes.data.repository.BaseLocalRepository
import com.jetbrains.notes.data.repository.LocalRepositoryImpl
import com.jetbrains.notes.ui.auth.AuthService
import com.jetbrains.notes.ui.auth.AuthServiceImpl
import com.jetbrains.notes.ui.auth.LoginViewModel
import com.jetbrains.notes.ui.auth.SignUpViewModel
import com.jetbrains.notes.ui.home.HomeViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
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
    single<AuthService>{
        AuthServiceImpl(Firebase.auth)
    }

//    viewModelOf(::HomeViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::HomeViewModel)
}

expect fun PlatformModule():Module
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
