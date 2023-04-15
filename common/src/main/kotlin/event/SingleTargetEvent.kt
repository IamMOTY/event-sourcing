package event

import domain.EntityWithId

abstract class SingleTargetEvent<Target : EntityWithId>(val targetId: Long?) : Event {

    fun apply(target: Target) {
        if (target.id == null || target.id == targetId) {
            applyImpl(target)
        }
    }

    abstract fun applyImpl(target: Target)
}