import domain.membership.error.MembershipNotExist
import domain.turnstile.Visit
import event.membership.RegisterMembershipEvent
import event.turnstile.EnterEvent
import event.turnstile.ExitEvent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import service.Service
import test.BaseTest
import util.toJson

class ReportServiceTest : BaseTest() {
    override fun initService(): Service = ReportService(storage)

    @Test
    fun averageDurationNoEvents_resultZero() = testWithEvents(
        RegisterMembershipEvent(0, "member", 100)
    ) {
        val actual = httpClient.req(
            "/report/average", mapOf(
                "id" to 0
            )
        )

        val expected =
            """
                { "result": 0.0 }
            """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun averageDurationMembershipNotExist_resultNotFound() {
        val actual = httpClient.req(
            "/report/average", mapOf(
                "id" to 0
            )
        )
        val expected = MembershipNotExist(0)
        assertEquals(expected.toJson(), actual)
    }

    @Test
    fun averageDurationCorrectData_resultSuccess() = testWithEvents(
        RegisterMembershipEvent(0, "member", Long.MAX_VALUE),
        EnterEvent(0, 100, 0),
        ExitEvent(0, 200, 0),
        EnterEvent(0, 300, 0),
        ExitEvent(0, 500, 0)
    ) {
        val actual = httpClient.req(
            "/report/average", mapOf(
                "id" to 0
            )
        )
        val expected =
            """
                { "result": 150.0 }
            """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun getVisitsBetweenNoVisits_resultEmpty() {
        val actual = httpClient.req(
            "/report/visits", mapOf(
                "from" to 0,
                "to" to Long.MAX_VALUE
            )
        )
        val expected = "[]"
        assertEquals(expected, actual)
    }

    @Test
    fun getVisitsBetweenCorrectData_resultSuccess() = testWithEvents(
        RegisterMembershipEvent(0, "member", Long.MAX_VALUE),
        EnterEvent(0, 0, 0),
        ExitEvent(0, 99, 0),
        EnterEvent(0, 100, 0),
        ExitEvent(0, 200, 0)
    ) {
        val actual = httpClient.req(
            "/report/visits", mapOf(
                "from" to 100,
                "to" to 200
            )
        )

        val expected = listOf(
            Visit().apply {
                memberId = 0
                enterAt = 100
                exitAt = 200
            }
        )

        assertEquals(expected.toJson(), actual)
    }
}