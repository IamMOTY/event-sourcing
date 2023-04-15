package command.turnstile

import command.Command
import event.turnstile.EnterEvent
import storage.EventWriteStorage

class EnterCommand(
    private val enterAt: Long,
    private val memberId: Long
) : Command {
    override fun execute(storage: EventWriteStorage) {
        storage.addEvent(EnterEvent(memberId, enterAt, memberId))
    }
}