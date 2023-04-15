import command.turnstile.EnterCommand
import command.turnstile.ExitCommand
import domain.membership.error.MembershipNotExist
import domain.membership.error.MembershipOverdue
import service.Service
import storage.EventReadStorage
import storage.EventWriteStorage
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import query.membership.GetMembershipByIdQuery
import java.time.Clock

class TurnstileService(
    private val writeStorage: EventWriteStorage,
    private val readStorage: EventReadStorage
) : Service {
    var clock = Clock.systemDefaultZone()

    override fun start(port: Int) {
        embeddedServer(Netty, port) {
            routing {
                get("/turnstile/enter") {
                    val params = context.request.queryParameters
                    val id = params["id"]!!.toLong()

                    call.respondText {
                        val membership = GetMembershipByIdQuery(id).execute(readStorage)
                        if (!membership.exists()) {
                            MembershipNotExist(id).toJson()
                        } else if (membership.expiresAt!! < clock.millis()) {
                            MembershipOverdue(id).toJson()
                        } else {
                            EnterCommand(clock.millis(), id).execute(writeStorage)
                            ""
                        }
                    }
                }

                get("/turnstile/exit") {
                    val params = context.request.queryParameters
                    val id = params["id"]!!.toLong()

                    call.respondText {
                        val membership = GetMembershipByIdQuery(id).execute(readStorage)
                        if (!membership.exists()) {
                            MembershipNotExist(id).toJson()
                        } else {
                            ExitCommand(id, clock.millis()).execute(writeStorage)
                            ""
                        }
                    }
                }
            }
        }.start(true)
    }
}