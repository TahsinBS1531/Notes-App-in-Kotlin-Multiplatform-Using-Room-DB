package com.jetbrains.notes.ui.auth

import dev.gitlive.firebase.auth.UserMetaData

data class User(
    val id: String = "",
    val isAnonymous: Boolean = true,
    val displayName:String?="",
    val phoneNumber : String?="",
    val email:String? ="",
    val metaData:UserMetaData? = null
)
