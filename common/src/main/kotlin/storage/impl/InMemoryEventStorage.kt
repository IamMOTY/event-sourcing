package storage.impl

import event.Event
import storage.EventReadStorage
import storage.EventWriteStorage
import java.util.concurrent.atomic.AtomicLong

class InMemoryEventStorage : EventWriteStorage, EventReadStorage {
    val events: MutableList<Event> = mutableListOf()
    private val newEventId: AtomicLong = AtomicLong(0)

    override fun addEvent(event: Event) {
        events.add(event.apply {
            eventId = newEventId.getAndIncrement()
        })
    }

    override fun loadEvents(): List<Event> = events
}