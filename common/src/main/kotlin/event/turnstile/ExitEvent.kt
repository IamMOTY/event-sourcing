package event.turnstile

import domain.turnstile.Visit

class ExitEvent(
    targetId: Long?,
    var exitAt: Long,
    var memberId: Long
) : TurnstileEvent(targetId) {
    override var eventId: Long? = null

    override fun applyImpl(target: Visit) {
        target.exitAt = exitAt
    }
}