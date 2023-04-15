package command.membership

import command.Command
import event.membership.RenewMembershipEvent
import storage.EventWriteStorage

class RenewMembershipCommand(
    val id: Long,
    private val expiresAt: Long
) : Command {
    override fun execute(storage: EventWriteStorage) {
        storage.addEvent(RenewMembershipEvent(id, expiresAt))
    }
}