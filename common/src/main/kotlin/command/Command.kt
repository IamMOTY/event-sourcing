package command

import storage.EventWriteStorage

interface Command {
    fun execute(storage: EventWriteStorage)
}