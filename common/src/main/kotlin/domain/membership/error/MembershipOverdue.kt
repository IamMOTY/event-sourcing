package domain.membership.error

import util.ToJson

data class MembershipOverdue(val id: Long) : ToJson {
    override fun toJson(): String =
        """
            { "error": "Membership with id $id is overdue" }
        """.trimIndent()
}