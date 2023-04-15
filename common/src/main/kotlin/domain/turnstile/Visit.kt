package domain.turnstile

import domain.EntityWithId
import util.ToJson

class Visit : EntityWithId(), ToJson {
    override var id: Long? = null
    var memberId: Long? = null
    var enterAt: Long? = null
    var exitAt: Long? = null

    override fun toJson(): String {
        return """
            {
                "id": $id,
                "member_id: $memberId,
                "enter": $enterAt,
                "exit": $exitAt
            }
        """.trimIndent()
    }
}
