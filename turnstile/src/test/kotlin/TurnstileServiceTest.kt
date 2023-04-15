import domain.membership.error.MembershipNotExist
import domain.membership.error.MembershipOverdue
import event.membership.RegisterMembershipEvent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import service.Service
import test.BaseTest
import java.time.Clock.*
import java.time.Instant.*
import java.time.ZoneOffset

class TurnstileServiceTest : BaseTest() {
    override fun initService(): Service {
        val service = TurnstileService(storage, storage)
        service.clock = fixed(ofEpochMilli(100), ZoneOffset.systemDefault())
        return service
    }

    @Test
    fun enterNonExistingMembership_resultNotFound() {
        val actual = httpClient.req(
            "/turnstile/enter", mapOf(
                "id" to 0
            )
        )

        val expected = MembershipNotExist(0)
        assertEquals(expected.toJson(), actual)
    }

    @Test
    fun exitNonExistingMembership_resultNotFound() {
        val actual = httpClient.req(
            "/turnstile/exit", mapOf(
                "id" to 0
            )
        )
        val expected = MembershipNotExist(0)
        assertEquals(expected.toJson(), actual)
    }

    @Test
    fun enterOverdueMembership_resultFail() = testWithEvents(
        RegisterMembershipEvent(0, "member", 0)
    ) {
        val actual = httpClient.req(
            "/turnstile/enter", mapOf(
                "id" to 0
            )
        )
        val expected = MembershipOverdue(0)
        assertEquals(expected.toJson(), actual)
    }

    @Test
    fun enterThenExitMembership_resultSuccess() = testWithEvents(
        RegisterMembershipEvent(0, "member", 100000)
    ) {
        httpClient.req(
            "/turnstile/enter", mapOf(
                "id" to 0
            )
        )

        val actual = httpClient.req(
            "/turnstile/exit", mapOf(
                "id" to 0
            )
        )

        assertEquals("", actual)
    }
}