package event.membership

import domain.membership.Membership
import event.SingleTargetEvent

class RenewMembershipEvent(
    targetId: Long,
    private val expiresAt: Long
) : MembershipEvent(targetId){
    override var eventId: Long? = null

    override fun applyImpl(target: Membership) {
        target.expiresAt = expiresAt
    }

}