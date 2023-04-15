import domain.membership.Membership
import domain.membership.error.MembershipAlreadyExist
import domain.membership.error.MembershipNotExist
import event.membership.RegisterMembershipEvent
import event.membership.RenewMembershipEvent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import service.Service
import test.BaseTest

class ManagerAdminServiceTest : BaseTest() {

    override fun initService(): Service = ManagerAdminService(storage, storage)

    @Test
    fun getMembershipTest_resultSuccess() = testWithEvents(
        RegisterMembershipEvent(0, "member", 100)
    ) {
        val expected = Membership().apply {
            id = 0
            memberName = "member"
            expiresAt = 100
        }

        val actual = httpClient.req(
            "/membership", mapOf(
                "id" to 0
            )
        )
        assertEquals(expected.toJson(), actual)
    }

    @Test
    fun renewMembershipTest_resultSuccess() = testWithEvents(
        RegisterMembershipEvent(0, "member", 100),
        RenewMembershipEvent(0, 200)
    ) {
        val expected = Membership().apply {
            id = 0
            memberName = "member"
            expiresAt = 200
        }

        val actual = httpClient.req(
            "/membership", mapOf(
                "id" to 0
            )
        )
        assertEquals(expected.toJson(), actual)
    }

    @Test
    fun renewNonExistMembership_resultNotFound() {
        val actual = httpClient.req(
            "/membership/renew", mapOf(
                "id" to 0,
                "expires" to 228
            )
        )
        val expected = MembershipNotExist(0)
        assertEquals(expected.toJson(), actual)
    }

    @Test
    fun registerMembershipAlreadyExists_resultError() = testWithEvents(
        RegisterMembershipEvent(0, "member", 100)
    ) {
        val actual = httpClient.req(
            "/membership/register", mapOf(
                "id" to 0,
                "member" to "member2",
                "expires" to 200
            )
        )
        val expected = MembershipAlreadyExist(0)
        assertEquals(expected.toJson(), actual)
    }

    @Test
    fun registerMembershipEvent_resultSuccess() {
        httpClient.req(
            "/membership/register", mapOf(
                "id" to 0,
                "member" to "member",
                "expires" to 100
            )
        )

        val actual = httpClient.req(
            "/membership", mapOf(
                "id" to 0
            )
        )

        val expected = Membership().apply {
            id = 0
            memberName = "member"
            expiresAt = 100
        }

        assertEquals(expected.toJson(), actual)
    }
}