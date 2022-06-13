package com.example.customer.repositories

import com.example.customer.entities.Location
import io.micronaut.data.annotation.Query
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Sort
import io.micronaut.data.model.query.builder.sql.Dialect
// import io.micronaut.data.annotation.Repository
// import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.transaction.annotation.TransactionalAdvice
import io.micronaut.data.repository.jpa.kotlin.CoroutineJpaSpecificationExecutor
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository

// @Repository(value = "customer-datasource")
// @JdbcRepository(dialect = Dialect.MYSQL)
@TransactionalAdvice("customer-datasource")
@R2dbcRepository(value = "customer-datasource", dialect = Dialect.MYSQL)
interface LocationRepository : CoroutineCrudRepository<Location, Int>, CoroutineJpaSpecificationExecutor<Location> {

    suspend fun findAll(pageable: Pageable): Page<Location>

    suspend fun findAll(sort: Sort): List<Location>

    @Query(
        """ 
            SELECT distinct l.*
            FROM locations l 
            JOIN customers c ON l.id = c.locationId
            JOIN sites s on c.id = s.customerId
            WHERE s.id = :siteId
        """
    )
    suspend fun getLocationBySite(siteId: Int): Location?

    suspend fun findByName(name: String): Location?

    suspend fun findByIsoCode(isoCode: String): Location?

    suspend fun findByParentIdNotEqual(parentId: Int): List<Location>

    suspend fun findByParentIdNotEqual(parentId: Int, pageable: Pageable): Page<Location>

    suspend fun findByNameLikeOrIsoCodeLikeOrderByName(
        name: String,
        isoCode: String,
        pageable: Pageable
    ): Page<Location>

    suspend fun updateByName(name: String, hasChildren: Boolean): Int
}
