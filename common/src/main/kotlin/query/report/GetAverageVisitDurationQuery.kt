package query.report

import domain.turnstile.Visit
import event.turnstile.EnterEvent
import event.turnstile.ExitEvent
import query.Query
import storage.EventReadStorage

class GetAverageVisitDurationQuery(val memberId: Long): Query<Double> {
    override fun execute(storage: EventReadStorage): Double {
        val enters = storage.loadEvents()
            .filterIsInstance<EnterEvent>()
            .filter { it.memberId == memberId}
            .sortedBy { it.eventId }

        val exits = storage.loadEvents()
            .filterIsInstance<ExitEvent>()
            .filter { it.memberId == memberId }
            .sortedBy { it.eventId }

        val zipped = enters.zip(exits)
        val visitsCount = zipped.size.toDouble()

        val sumDuration = zipped.map { (entr, ext) ->
            val visit = Visit()
            entr.apply(visit)
            ext.apply(visit)
            visit.exitAt!! - visit.enterAt!!
        }.sum().toDouble()

        return if (visitsCount == 0.0) 0.0 else sumDuration / visitsCount
    }
}