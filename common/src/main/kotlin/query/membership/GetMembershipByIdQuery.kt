package query.membership

import domain.membership.Membership
import event.membership.MembershipEvent
import query.Query
import storage.EventReadStorage

class GetMembershipByIdQuery(private val id: Long) : Query<Membership> {
    override fun execute(storage: EventReadStorage): Membership {
        val result = Membership()
        storage.loadEvents()
            .filterIsInstance<MembershipEvent>()
            .sortedBy { it.eventId }
            .filter { it.targetId == id}
            .forEach { it.apply(result) }
        return result
    }
}