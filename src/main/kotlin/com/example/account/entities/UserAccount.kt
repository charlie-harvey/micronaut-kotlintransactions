package com.example.account.entities

import com.example.common.OptimizedUUID
import io.micronaut.core.annotation.Introspected
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.model.naming.NamingStrategies
import java.time.LocalDateTime
import javax.validation.constraints.Pattern

@Introspected
@MappedEntity(value = "userAccounts", namingStrategy = NamingStrategies.Raw::class)
data class UserAccount(
    @field:Id
    var id: OptimizedUUID,
    var siteId: Int,
    var username: String,
    var firstName: String,
    var lastName: String,
    @field:Pattern(regexp = "en|es", message = "Invalid Language")
    var primaryLanguage: String = "en",
    @field:Pattern(regexp = "en|es", message = "Invalid Language")
    var applicationLanguage: String = "en",
    var active: Boolean,
    var tobedeleted: Boolean,
    var forcePasswordReset: Boolean,
    var passwordSetByRoster: Boolean,
    var externalSourceId: String,
    var password: String?,
    var userDefinedPassword: String?,
    var dateSetDeleted: LocalDateTime?,
    var currentGrade: String?,
)
