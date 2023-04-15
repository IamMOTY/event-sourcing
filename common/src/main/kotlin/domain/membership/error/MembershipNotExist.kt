package domain.membership.error

import util.ToJson

data class MembershipNotExist(val id: Long) : ToJson {
    override fun toJson(): String =
        """
            { "error": "Membership with id $id does not exist" }
        """.trimIndent()
}