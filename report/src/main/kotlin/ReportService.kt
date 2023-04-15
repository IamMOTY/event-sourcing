import domain.membership.error.MembershipNotExist
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import query.membership.GetMembershipByIdQuery
import query.report.GetAverageVisitDurationQuery
import query.report.GetVisitsBetweenQuery
import service.Service
import storage.EventReadStorage
import storage.EventWriteStorage
import util.toJson

class ReportService(
    private val readStorage: EventReadStorage
) : Service {
    override fun start(port: Int) {
        embeddedServer(Netty, port) {
            routing {
                get("/report/average") {
                    val params = context.request.queryParameters
                    val id = params["id"]!!.toLong()

                    call.respondText {
                        val membership = GetMembershipByIdQuery(id).execute(readStorage)

                        if (!membership.exists()) {
                            MembershipNotExist(id).toJson()
                        } else {
                            val avg = GetAverageVisitDurationQuery(id).execute(readStorage)
                            """
                                { "result": $avg }
                            """.trimIndent()
                        }
                    }
                }

                get("/report/visits") {
                    val params = context.request.queryParameters
                    val from = params["from"]!!.toLong()
                    val to = params["to"]!!.toLong()

                    call.respondText {
                        GetVisitsBetweenQuery(from, to).execute(readStorage).toJson()
                    }
                }
            }
        }.start(true)
    }
}