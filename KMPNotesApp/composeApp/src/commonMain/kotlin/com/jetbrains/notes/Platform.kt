package com.jetbrains.notes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform