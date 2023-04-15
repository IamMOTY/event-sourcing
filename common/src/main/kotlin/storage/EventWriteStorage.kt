package storage

import event.Event

interface EventWriteStorage {
    fun addEvent(event: Event)
}