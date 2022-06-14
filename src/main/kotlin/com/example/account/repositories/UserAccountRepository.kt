package com.example.account.repositories

import com.example.db.OptimizedUUID
import com.example.account.entities.UserAccount
import io.micronaut.data.annotation.Id
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
// import io.micronaut.data.annotation.Repository
// import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.transaction.annotation.TransactionalAdvice
import io.micronaut.data.repository.jpa.kotlin.CoroutineJpaSpecificationExecutor
import io.micronaut.data.repository.kotlin.CoroutineCrudRepository
import kotlinx.coroutines.flow.Flow
import javax.transaction.Transactional
import javax.validation.constraints.NotNull

// @Repository(value = "account-datasource")
// @JdbcRepository(dialect = Dialect.MYSQL)
@TransactionalAdvice("accounts-datasource")
@R2dbcRepository(value = "accounts-datasource", dialect = Dialect.MYSQL)
interface UserAccountRepository : CoroutineCrudRepository<UserAccount, OptimizedUUID>,
    CoroutineJpaSpecificationExecutor<UserAccount> {

    override fun findAll(): Flow<UserAccount>

    suspend fun findAll(pageable: Pageable): Page<UserAccount>

    suspend fun findAll(sort: io.micronaut.data.model.Sort): Iterable<UserAccount>

    override suspend fun findById(id: @NotNull OptimizedUUID): UserAccount?

    suspend fun findBySiteIdAndId(siteId: Int, id: OptimizedUUID): UserAccount?

    suspend fun findBySiteIdAndIdIn(siteId: Int, ids: Collection<OptimizedUUID>): List<UserAccount>

    suspend fun findBySiteIdAndUsername(
        siteId: Int,
        username: String
    ): UserAccount?

    @Transactional(Transactional.TxType.MANDATORY)
    suspend fun deleteByUsernameAndSiteId(username: String, siteId: Int): Int

    @Transactional(Transactional.TxType.MANDATORY)
    suspend fun updateByIdInListAndSiteId(id: List<OptimizedUUID>, siteId: Int, applicationLanguage: String): Int

    @Transactional(Transactional.TxType.MANDATORY)
    suspend fun updatePassword(
        @Id id: OptimizedUUID,
        password: String,
        tobedeleted: Boolean,
        forcePasswordReset: Boolean
    )

    @Transactional(Transactional.TxType.MANDATORY)
    suspend fun updateAll(userAccounts: Iterable<UserAccount>): Int

    @Transactional(Transactional.TxType.MANDATORY)
    override suspend fun <S : UserAccount> save(entity: S): S

    @Transactional(Transactional.TxType.MANDATORY)
    override fun <S : UserAccount> saveAll(entities: Iterable<S>): Flow<S>

    @Transactional(Transactional.TxType.MANDATORY)
    override suspend fun <S : UserAccount> update(entity: S): S

    @Transactional(Transactional.TxType.MANDATORY)
    override fun <S : UserAccount> updateAll(entities: Iterable<S>): Flow<S>

    @Transactional(Transactional.TxType.MANDATORY)
    override suspend fun deleteById(id: OptimizedUUID): Int

    @Transactional(Transactional.TxType.MANDATORY)
    override suspend fun delete(entity: UserAccount): Int

    @Transactional(Transactional.TxType.MANDATORY)
    override suspend fun deleteAll(entities: Iterable<UserAccount>): Int

    @Transactional(Transactional.TxType.MANDATORY)
    override suspend fun deleteAll(): Int
}
