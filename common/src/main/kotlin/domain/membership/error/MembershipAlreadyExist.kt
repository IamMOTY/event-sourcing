package domain.membership.error

import util.ToJson

data class MembershipAlreadyExist(val id: Long) : ToJson {
    override fun toJson(): String =
        """
            { "error": Membership with id $id already exists }
        """.trimIndent()
}