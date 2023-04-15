package event.turnstile

import domain.turnstile.Visit

class EnterEvent(
    targetId: Long?,
    var enterAt: Long,
    var memberId: Long
) : TurnstileEvent(targetId) {
    override var eventId: Long? = null

    override fun applyImpl(target: Visit) {
        target.enterAt = enterAt
        target.memberId = memberId
    }

}