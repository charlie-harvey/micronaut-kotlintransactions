package com.example.customer.repositories

import com.example.common.OptimizedUUID
import com.example.customer.entities.Customer
import io.micronaut.data.annotation.Join
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
import kotlinx.coroutines.flow.Flow
import javax.transaction.Transactional

// @Repository(value = "customer-datasource")
// @JdbcRepository(dialect = Dialect.MYSQL)
@TransactionalAdvice("customer-datasource")
@R2dbcRepository(value = "customer-datasource", dialect = Dialect.MYSQL)
interface CustomerRepository : CoroutineCrudRepository<Customer, OptimizedUUID>,
    CoroutineJpaSpecificationExecutor<Customer> {

    @Transactional(Transactional.TxType.MANDATORY)
    override suspend fun <S : Customer> save(entity: S): S

    @Transactional(Transactional.TxType.MANDATORY)
    override fun <S : Customer> saveAll(entities: Iterable<S>): Flow<S>

    @Transactional(Transactional.TxType.MANDATORY)
    override suspend fun <S : Customer> update(entity: S): S

    @Transactional(Transactional.TxType.MANDATORY)
    override fun <S : Customer> updateAll(entities: Iterable<S>): Flow<S>

    @Transactional(Transactional.TxType.MANDATORY)
    override suspend fun deleteById(id: OptimizedUUID): Int

    @Transactional(Transactional.TxType.MANDATORY)
    override suspend fun delete(entity: Customer): Int

    @Transactional(Transactional.TxType.MANDATORY)
    override suspend fun deleteAll(entities: Iterable<Customer>): Int

    @Transactional(Transactional.TxType.MANDATORY)
    override suspend fun deleteAll(): Int

    @Join(value = "location")
    suspend fun findAll(pageable: Pageable): Page<Customer>

    @Join(value = "location")
    suspend fun findAll(sort: Sort): Set<Customer>

    @Join(value = "location")
    suspend fun findByExternalCustomerId(externalCustomerId: String): Customer?
}
