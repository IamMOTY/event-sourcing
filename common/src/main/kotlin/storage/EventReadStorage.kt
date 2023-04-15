package storage

import event.Event

interface EventReadStorage {
    fun loadEvents(): List<Event>
}