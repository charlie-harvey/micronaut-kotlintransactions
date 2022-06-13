package com.example.customer.service

import com.example.customer.entities.Location
import com.example.customer.repositories.LocationRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Sort
import io.micronaut.transaction.annotation.TransactionalAdvice
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.toList
import javax.transaction.Transactional

@Singleton
open class LocationService(
    private val locationRepository: LocationRepository
) {

    suspend fun findAll(): List<Location> =
        locationRepository.findAll().toList()

    suspend fun findById(id: Int): Location? =
        locationRepository.findById(id)

    suspend fun findBySiteId(siteId: Int): Location? =
        locationRepository.getLocationBySite(siteId)

    suspend fun findByName(name: String): Location? =
        locationRepository.findByName(name)

    suspend fun findByIsoCode(isoCode: String): Location? =
        locationRepository.findByIsoCode(isoCode)

    suspend fun searchLocations(searchTerm: String): Page<Location> =
        locationRepository.findByNameLikeOrIsoCodeLikeOrderByName(
            name = "%${searchTerm}%",
            isoCode = "%${searchTerm}%",
            Pageable.from(0, 500)
        )

    suspend fun getPaginatedLocations(page: Int, parentId: Int? = null): Page<Location> {
        val pageable = Pageable.from(page, 100, Sort.of(Sort.Order.asc("name")))
        return if (parentId == null) {
            locationRepository.findAll(pageable)
        } else {
            locationRepository.findByParentIdNotEqual(parentId = 0, pageable)
        }
    }

    suspend fun getStates(): List<Location> =
        locationRepository.findByParentIdNotEqual(parentId = 0)

    @Transactional
    @TransactionalAdvice("customer-datasource")
    open suspend fun save(location: Location): Location =
        locationRepository.save(location)

    @Transactional
    @TransactionalAdvice("customer-datasource")
    open suspend fun update(location: Location): Location =
        locationRepository.update(location)

    @Transactional
    @TransactionalAdvice("customer-datasource")
    open suspend fun deleteById(id: Int) {
        val locationToDelete = locationRepository.findById(id)
        if (locationToDelete != null) {
            locationRepository.deleteById(id)
        }
    }

    @Transactional
    @TransactionalAdvice("customer-datasource")
    open suspend fun updateHasChildrenByName(name: String, hasChildren: Boolean): Int =
        locationRepository.updateByName(name, hasChildren)
}
