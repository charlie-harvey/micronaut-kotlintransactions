package com.example.customer.entities

import com.example.common.OptimizedUUID
import io.micronaut.core.annotation.Introspected
import io.micronaut.data.annotation.*
import io.micronaut.data.model.naming.NamingStrategies

@Introspected
@MappedEntity(value = "customers", namingStrategy = NamingStrategies.Raw::class)
data class Customer(
    @field:Id
    var id: OptimizedUUID,
    var name: String,
    var externalCustomerId: String,
    @field:Relation(value = Relation.Kind.ONE_TO_ONE)
    @field:MappedProperty("locationId")
    var location: Location,
    var url: String,
    var totalSchools: Int,
    var studentEnrollment: Int
) {
    @Transient
    var licenseCount: Int = 0
}
