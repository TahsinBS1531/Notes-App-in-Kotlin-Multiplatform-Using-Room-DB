package com.jetbrains.notes.data.di

import com.jetbrains.notes.data.model.local.AppDatabase
import com.jetbrains.notes.getDatabaseBuilder
import dev.icerock.moko.permissions.PermissionsController
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun PlatformModule(): Module {
    return module {
//        single { get<Context>() }
        single {
            getDatabaseBuilder(get()) // Pass Android context here
        }
        single{
            PermissionsController(get())
        }
        // Provide DAO
        single {
            get<AppDatabase>().getDao()
        }
    }
}