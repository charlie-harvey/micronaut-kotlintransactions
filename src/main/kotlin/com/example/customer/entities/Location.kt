package com.example.customer.entities

import io.micronaut.core.annotation.Introspected
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.model.naming.NamingStrategies

@Introspected
@MappedEntity(value = "locations", namingStrategy = NamingStrategies.Raw::class)
data class Location(
    @field:Id
    @field:GeneratedValue
    var id: Int? = null,
    var name: String,
    var isoCode: String,
    var parentId: Int,
    var hasChildren: Boolean
)
