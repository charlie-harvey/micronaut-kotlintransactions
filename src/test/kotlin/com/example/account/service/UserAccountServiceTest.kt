package com.example.account.service

import com.example.account.domain.CreateUserAccountObject
import com.example.account.entities.UserAccount
import com.example.account.repositories.UserAccountRepository
import com.example.common.createUserAccountObjectArb
import com.example.common.userAccountEntityArb
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.arbitrary.*
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.micronaut.transaction.annotation.TransactionalAdvice
import io.mockk.clearAllMocks
import kotlinx.coroutines.runBlocking
import javax.transaction.Transactional

@MicronautTest(startApplication = false, transactional = false)
open class UserAccountServiceTest(
    private val userAccountService: UserAccountService,
    private val userAccountRepository: UserAccountRepository
) : AnnotationSpec() {

    private lateinit var createUserAccountObject: CreateUserAccountObject
    private lateinit var validUserAccount: UserAccount

    private val siteId = 10000

    @Transactional
    @TransactionalAdvice("accounts-datasource")
    @BeforeEach
    open fun beforeEach() = runBlocking {
        createUserAccountObject = createUserAccountObjectArb(siteId = siteId, grade = "4").single()
        userAccountService.deleteByUsernameAndSiteId(createUserAccountObject.username, createUserAccountObject.siteId)
        validUserAccount = userAccountService.createUserAccount(createUserAccountObject)
    }

    @Transactional
    @TransactionalAdvice("accounts-datasource")
    @AfterEach
    open fun afterEach() = runBlocking {
        userAccountService.deleteByUsernameAndSiteId(createUserAccountObject.username, createUserAccountObject.siteId)
        clearAllMocks()
    }

    @Test
    suspend fun findByUsername() {
        val ua = userAccountEntityArb(
            siteId = validUserAccount.siteId,
            tobedeleted = false,
            active = true,
            username = "pants"
        ).next();
        userAccountRepository.save(ua)

        userAccountService.findBySiteIdAndUsername(
            siteId = validUserAccount.siteId,
            username = "pants"
        ).shouldBe(ua)

        userAccountService.deleteByUsernameAndSiteId(ua.username, ua.siteId)
    }
}
