package domain.membership

import domain.EntityWithId
import domain.turnstile.Visit
import util.ToJson

class Membership : EntityWithId(), ToJson {
    override var id: Long? = null
    var memberName: String? = null
    var expiresAt: Long? = null

    override fun toJson(): String {
        return """
            {
                "id": $id,
                "member": $memberName,
                "expires": $expiresAt
            }
        """.trimIndent()
    }
}
