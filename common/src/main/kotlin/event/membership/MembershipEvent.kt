package event.membership

import domain.membership.Membership
import event.Event
import event.SingleTargetEvent

abstract class MembershipEvent(targetId: Long) : SingleTargetEvent<Membership>(targetId)