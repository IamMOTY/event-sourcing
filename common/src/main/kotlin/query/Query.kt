package query

import storage.EventReadStorage

interface Query<T> {
    fun execute(storage: EventReadStorage): T
}