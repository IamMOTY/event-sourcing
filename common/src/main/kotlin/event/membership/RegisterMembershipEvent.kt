package event.membership

import domain.membership.Membership
import event.SingleTargetEvent

class RegisterMembershipEvent(
    targetId: Long,
    private val memberName: String,
    private val expiresAt: Long
) : MembershipEvent(targetId) {
    override var eventId: Long? = null

    override fun applyImpl(target: Membership) {
        target.id = targetId
        target.memberName = memberName
        target.expiresAt = expiresAt
    }
}