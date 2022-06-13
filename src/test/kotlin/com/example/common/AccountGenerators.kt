package com.example.common

import com.example.account.domain.CreateUserAccountObject
import com.example.account.entities.UserAccount
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import java.time.LocalDateTime
import java.util.*

fun alphaNumericStringArb(min: Int, max: Int): Arb<String> = Arb.stringPattern("[a-zA-Z0-9]{$min,$max}")
val alphaNumericFiveToTwentyArb: Arb<String> = alphaNumericStringArb(5, 20)

val gradeArb: Arb<String> = Arb.nats(13).map { it.toString() }.merge(arbitrary { "K" })
val langArb: Arb<String> = Arb.element("en", "es")
val uuidArb = Arb.uuid(UUIDVersion.V1, false)
val emailArb: Arb<String> = Arb.email(alphaNumericStringArb(1, 100), alphaNumericStringArb(1, 100))

val optimizedUuidArb: Arb<OptimizedUUID> = Arb.bind(uuidArb, Arb.string()) { uuid, _ -> OptimizedUUID(uuid) }

val siteIdArb = Arb.int(10_000, 20_100)

fun userAccountEntityArb(
    id: OptimizedUUID? = null,
    siteId: Int? = null,
    username: String? = null,
    firstName: String? = null,
    lastName: String? = null,
    primaryLanguage: String? = null,
    active: Boolean? = null,
    tobedeleted: Boolean? = null,
    forcePasswordReset: Arb<Boolean> = Arb.bool(),
    passwordSetByRoster: Arb<Boolean> = Arb.bool(),
    externalSourceId: String? = null,
    password: String? = null,
    userDefinedPassword: String? = null,
    dateSetDeleted: Arb<LocalDateTime>? = Arb.localDateTime(
        minYear = LocalDateTime.now().year,
        maxYear = LocalDateTime.now().year
    ),
    currentGrade: String? = null
): Arb<UserAccount> = Arb.bind(
    uuidArb, // id
    siteIdArb, // siteId
    alphaNumericFiveToTwentyArb, // username
    alphaNumericFiveToTwentyArb, // firstName
    alphaNumericFiveToTwentyArb, // lastName
    langArb, // primaryLanguage
    Arb.bool(), // active
    Arb.bool(), // tobedeleted
    alphaNumericFiveToTwentyArb, // externalSourceId
    alphaNumericFiveToTwentyArb, // password
    alphaNumericFiveToTwentyArb, // userDefinedPassword
    gradeArb // currentGrade
) { idB, siteIdB, usernameB, firstNameB, lastNameB, primaryLanguageB,
    activeB, tobedeletedB, passwordB, userDefinedPasswordB, externalSourceIdB, currentGradeB ->
    UserAccount(
        id = id ?: OptimizedUUID(idB),
        siteId = siteId ?: siteIdB,
        username = username ?: usernameB,
        firstName = firstName ?: firstNameB,
        lastName = lastName ?: lastNameB,
        primaryLanguage = primaryLanguage ?: primaryLanguageB,
        active = active ?: activeB,
        tobedeleted = tobedeleted ?: tobedeletedB,
        forcePasswordReset = forcePasswordReset.next(),
        passwordSetByRoster = passwordSetByRoster.next(),
        externalSourceId = externalSourceId ?: externalSourceIdB,
        password = password ?: passwordB,
        userDefinedPassword = userDefinedPassword ?: userDefinedPasswordB,
        dateSetDeleted = dateSetDeleted?.next(),
        currentGrade = currentGrade ?: currentGradeB
    )
}

fun createUserAccountObjectArb(
    id: UUID? = null,
    siteId: Int? = null,
    userId: UUID? = null,
    username: String? = null,
    firstName: String? = null,
    lastName: String? = null,
    primaryLanguage: String? = null,
    password: String? = null,
    userDefinedPassword: String? = null,
    passwordSetByRoster: Boolean? = null,
    externalSourceId: String? = null,
    grade: String? = null,
    rosterEmail: String? = null,
): Arb<CreateUserAccountObject> = Arb.bind(
    siteIdArb, // siteId
    uuidArb, // userId
    alphaNumericFiveToTwentyArb, // username
    alphaNumericFiveToTwentyArb, // firstName
    alphaNumericFiveToTwentyArb, // lastName
    langArb, // primaryLanguage
    alphaNumericFiveToTwentyArb, // password,
    alphaNumericFiveToTwentyArb, // userDefinedPassword
    Arb.bool(), // passwordSetByRoster
    alphaNumericFiveToTwentyArb, // externalSourceId
    gradeArb, // grade
    emailArb // roster email
) { siteIdB, userIdB, usernameB, firstNameB, lastNameB, primaryLanguageB,
    passwordB, userDefinedPasswordB, passwordSetByRosterB, externalSourceIdB, gradeB, rosterEmailB ->
    CreateUserAccountObject(
        id = id,
        siteId = siteId ?: siteIdB,
        userId = userId ?: userIdB,
        username = username ?: usernameB,
        firstName = firstName ?: firstNameB,
        lastName = lastName ?: lastNameB,
        primaryLanguage = primaryLanguage ?: primaryLanguageB,
        password = password ?: passwordB,
        userDefinedPassword = userDefinedPassword ?: userDefinedPasswordB,
        passwordSetByRoster = passwordSetByRoster ?: passwordSetByRosterB,
        externalSourceId = externalSourceId ?: externalSourceIdB,
        grade = grade ?: gradeB,
        rosterEmail = rosterEmail ?: rosterEmailB
    )
}
