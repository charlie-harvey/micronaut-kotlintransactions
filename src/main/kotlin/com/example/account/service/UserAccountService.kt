package com.example.account.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.account.domain.CreateUserAccountObject
import com.example.account.entities.UserAccount
import com.example.account.repositories.UserAccountRepository
import com.example.db.OptimizedUUID
import com.github.f4b6a3.uuid.UuidCreator
import io.micronaut.transaction.annotation.TransactionalAdvice
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.transaction.Transactional
import javax.validation.ConstraintViolationException

@Singleton
open class UserAccountService(
    private val userAccountRepository: UserAccountRepository
) {

    private val hasher: BCrypt.Hasher = BCrypt.withDefaults()

    suspend fun findById(id: UUID): UserAccount? =
        userAccountRepository.findById(id = OptimizedUUID(id))

    suspend fun findBySiteIdAndId(siteId: Int, id: UUID): UserAccount? =
        userAccountRepository.findBySiteIdAndId(siteId = siteId, id = OptimizedUUID(id))

    suspend fun findBySiteIdAndIdsIn(siteId: Int, ids: Collection<UUID>): List<UserAccount> =
        userAccountRepository.findBySiteIdAndIdIn(siteId = siteId, ids = ids.map { OptimizedUUID(it) })

    @Transactional
    @TransactionalAdvice("accounts-datasource")
    open suspend fun createUserAccount(createRequest: CreateUserAccountObject): UserAccount {
        val userAccountId = if (createRequest.id != null) {
            OptimizedUUID(createRequest.id)
        } else {
            OptimizedUUID(UuidCreator.getTimeBased())
        }
        val hashedPasswd: String? = createRequest.password?.let {
            hasher.hashToString(10, it.toCharArray())
        }
        val hashedUserDefinedPassword: String? = createRequest.userDefinedPassword?.let {
            hasher.hashToString(10, it.toCharArray())
        }
        val userAccount = UserAccount(
            id = userAccountId,
            siteId = createRequest.siteId,
            username = createRequest.username,
            firstName = createRequest.firstName,
            lastName = createRequest.lastName,
            primaryLanguage = createRequest.primaryLanguage,
            // initially default application language to primary language
            applicationLanguage = createRequest.applicationLanguage ?: createRequest.primaryLanguage,
            active = true,
            tobedeleted = false,
            forcePasswordReset = false,
            passwordSetByRoster = createRequest.passwordSetByRoster,
            externalSourceId = createRequest.externalSourceId,
            password = hashedPasswd,
            userDefinedPassword = hashedUserDefinedPassword,
            dateSetDeleted = null,
            currentGrade = createRequest.grade
        )
        return userAccountRepository.save(userAccount)
    }

    suspend fun findBySiteIdAndUsername(siteId: Int, username: String) =
        userAccountRepository.findBySiteIdAndUsername(
            siteId = siteId,
            username = username
        )

    @Transactional
    @TransactionalAdvice("accounts-datasource")
    open suspend fun deleteByUsernameAndSiteId(username: String, siteId: Int) {
        userAccountRepository.deleteByUsernameAndSiteId(username, siteId)
    }

    @Transactional
    @TransactionalAdvice("accounts-datasource")
    open suspend fun changeUsersPassword(userId: UUID, password: String): Boolean {
        val hashedPassword = BCrypt.withDefaults().hashToString(10, password.toCharArray())
        return try {
            userAccountRepository.updatePassword(
                id = OptimizedUUID(userId), password = hashedPassword, tobedeleted = false, forcePasswordReset = true
            )
            true
        } catch (ex: ConstraintViolationException) {
            false
        }
    }

    fun findAll(): Flow<UserAccount> = userAccountRepository.findAll()

    @Transactional
    @TransactionalAdvice("accounts-datasource")
    open suspend fun updateUserAccounts(userAccounts: List<UserAccount>) =
        userAccountRepository.updateAll(userAccounts)

    @Transactional
    @TransactionalAdvice("accounts-datasource")
    open suspend fun save(userAccount: UserAccount) =
        userAccountRepository.save(userAccount)
}
