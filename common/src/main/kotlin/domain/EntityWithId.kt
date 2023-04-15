package domain

abstract class EntityWithId {
    abstract val id: Long?

    fun exists(): Boolean {
        return id != null
    }
}