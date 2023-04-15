package query.report

import domain.turnstile.Visit
import event.turnstile.EnterEvent
import event.turnstile.ExitEvent
import query.Query
import storage.EventReadStorage

class GetVisitsBetweenQuery(
    private val from: Long,
    private val to: Long
) : Query<List<Visit>> {
    override fun execute(storage: EventReadStorage): List<Visit> {
        val enters = storage.loadEvents()
            .filterIsInstance<EnterEvent>()
            .filter { it.enterAt in from..to }
            .groupBy { it.memberId }
            .mapValues { (_, vs) -> vs.sortedBy { ev -> ev.eventId } }

        val exits = storage.loadEvents()
            .filterIsInstance<ExitEvent>()
            .filter { it.exitAt in from..to }
            .groupBy { it.memberId }
            .mapValues { (_, vs) -> vs.sortedBy { ev -> ev.eventId } }

        val results: MutableList<Visit> = mutableListOf()
        enters.forEach { (mName, entrs) ->
            val exts = exits.getOrDefault(mName, listOf())
            val zipped: List<Pair<EnterEvent, ExitEvent>> = entrs.zip(exts)

            val result = zipped
                .map { (entr: EnterEvent, ext: ExitEvent) ->
                    val visit = Visit()
                    entr.apply(visit)
                    ext.apply(visit)
                    visit
            }.toMutableList()

            if (entrs.size == exts.size + 1) {
                val visit = Visit()
                entrs.last().apply(visit)
                result.add(visit)
            }

            results.addAll(result)
        }

        return results
    }
}