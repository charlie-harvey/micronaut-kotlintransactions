package com.example.customer.service

import com.example.common.OptimizedUUID
import com.example.customer.entities.Customer
import com.example.customer.repositories.CustomerRepository
import com.github.f4b6a3.uuid.UuidCreator
import io.micronaut.data.model.Sort
import io.micronaut.transaction.annotation.TransactionalAdvice
import java.util.*
import jakarta.inject.Singleton
import javax.transaction.Transactional

@Singleton
open class CustomerService(
    private val customerRepository: CustomerRepository,
    private val locationService: LocationService
) {

    suspend fun findById(id: UUID): Customer? =
        customerRepository.findById(OptimizedUUID(id))

    suspend fun findAll(): List<Customer> =
        customerRepository.findAll(Sort.of(Sort.Order.asc("name")))
            .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })

    @Transactional
    @TransactionalAdvice("customer-datasource")
    open suspend fun save(customer: Customer): Customer =
        customerRepository.save(customer)

    @Transactional
    @TransactionalAdvice("customer-datasource")
    open suspend fun deleteById(customerId: UUID) {
        customerRepository.deleteById(OptimizedUUID(customerId))
    }

    @Transactional
    @TransactionalAdvice("customer-datasource")
    open suspend fun createAndSaveCustomer(
        name: String,
        locationCode: String,
        url: String = "",
        totalSchools: Int = 0,
        studentEnrollment: Int = 0,
        externalCustomerId: String
    ): Customer {
        val location = locationService.findByIsoCode(locationCode) ?: throw Exception("Unknown location")

        val customerMatch = customerRepository.findByExternalCustomerId(externalCustomerId)
        if (customerMatch != null) {
            var shouldUpdateCustomer = false

            if(customerMatch.location != location) {
                customerMatch.location = location
                shouldUpdateCustomer = true
            }
            if(customerMatch.name != name) {
                customerMatch.name = name
                shouldUpdateCustomer = true
            }
            if(customerMatch.studentEnrollment != studentEnrollment) {
                customerMatch.studentEnrollment = studentEnrollment
                shouldUpdateCustomer = true
            }
            if(customerMatch.totalSchools != totalSchools) {
                customerMatch.totalSchools = totalSchools
                shouldUpdateCustomer = true
            }
            if(customerMatch.url != url) {
                customerMatch.url = url
                shouldUpdateCustomer = true
            }
            if(shouldUpdateCustomer) {
                customerRepository.update(customerMatch)
            }
            return customerMatch
        } else {
            val newCustomer = Customer(
                id = OptimizedUUID(UuidCreator.getTimeBased()),
                name = name,
                studentEnrollment = studentEnrollment,
                totalSchools = totalSchools,
                url = url,
                location = location,
                externalCustomerId = externalCustomerId
            )
            customerRepository.save(newCustomer)
            return newCustomer
        }
    }
}
