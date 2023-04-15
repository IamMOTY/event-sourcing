package command.membership

import command.Command
import event.membership.RegisterMembershipEvent
import storage.EventWriteStorage

class RegisterMembershipCommand(
    val id: Long,
    private val memberName: String,
    private val expiresAt: Long
) : Command {
    override fun execute(storage: EventWriteStorage) {
        storage.addEvent(RegisterMembershipEvent(id, memberName, expiresAt))
    }
}