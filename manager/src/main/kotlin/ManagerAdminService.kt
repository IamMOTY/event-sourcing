import command.membership.RegisterMembershipCommand
import command.membership.RenewMembershipCommand
import domain.membership.error.MembershipAlreadyExist
import domain.membership.error.MembershipNotExist
import storage.EventReadStorage
import storage.EventWriteStorage
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import query.membership.GetMembershipByIdQuery
import service.Service

class ManagerAdminService(
    private val writeStorage: EventWriteStorage,
    private val readStorage: EventReadStorage
) : Service {
    override fun start(port: Int) {
        embeddedServer(Netty, port) {
            routing {
                get("/membership/register") {
                    val params = context.request.queryParameters
                    val id = params["id"]!!.toLong()
                    val memberName = params["member"]!!.toString()
                    val expiresAt = params["expires"]!!.toLong()

                    val membership = GetMembershipByIdQuery(id).execute(readStorage)

                    call.respondText {
                        if (membership.exists()) {
                            MembershipAlreadyExist(id).toJson()
                        } else {
                            RegisterMembershipCommand(id, memberName, expiresAt)
                                .execute(writeStorage)
                            context.response.status(HttpStatusCode.Created)
                            ""
                        }
                    }
                }

                get("/membership") {
                    val params = context.request.queryParameters
                    val id = params["id"]!!.toLong()
                    val res = GetMembershipByIdQuery(id).execute(readStorage)
                    if (!res.exists()) {
                        context.response.status(HttpStatusCode.NotFound)
                        call.respondText {
                            MembershipNotExist(id).toJson()
                        }
                    } else {
                        call.respondText {
                            res.toJson()
                        }
                    }
                }

                get("/membership/renew") {
                    val params = context.request.queryParameters
                    val id = params["id"]!!.toLong()
                    val expiresAt = params["expires"]!!.toLong()

                    val membership = GetMembershipByIdQuery(id).execute(readStorage)

                    call.respondText {
                        if (!membership.exists()) {
                            MembershipNotExist(id).toJson()
                        } else {
                            RenewMembershipCommand(id, expiresAt).execute(writeStorage)
                            val res = GetMembershipByIdQuery(id).execute(readStorage)
                            res.toJson()
                        }
                    }

                }

            }
        }.start(true)
    }
}