package com.example.account.domain

import io.micronaut.core.annotation.Introspected
import java.util.*

@Introspected
data class CreateUserAccountObject(
    val id: UUID? = null,
    val siteId: Int,
    val userId: UUID,
    val username: String,
    val firstName: String,
    val lastName: String,
    val primaryLanguage: String,
    val applicationLanguage: String? = null,
    var password: String? = null,
    var userDefinedPassword: String? = null,
    val passwordSetByRoster: Boolean,
    val externalSourceId: String,
    val grade: String?,
    val rosterEmail: String? = null
)