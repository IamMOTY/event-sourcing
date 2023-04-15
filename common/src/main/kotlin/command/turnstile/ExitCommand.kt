package command.turnstile

import command.Command
import event.turnstile.ExitEvent
import storage.EventWriteStorage

class ExitCommand(
    private val exitAt: Long,
    private val memberId: Long
) : Command {
    override fun execute(storage: EventWriteStorage) {
        storage.addEvent(ExitEvent(memberId, exitAt, memberId))
    }
}