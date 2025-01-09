package com.al3x.housing2.api

import com.al3x.housing2.Action.Action
import com.al3x.housing2.Action.HTSLImpl
import com.al3x.housing2.Main
import com.al3x.housing2.Utils.Duple
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class Housing2Api {
    companion object {
        val instance = Housing2Api()
        //UUID, <Secret Key, Action>
        val editing: HashMap<String, Duple<String, Action>> = HashMap()
    }
    fun start() {
        println("Housing2Api starting")
        embeddedServer(Netty, port = Main.getInstance().config.getInt("api_port", 8080)) {
            routing {
                get("/") {
                    call.respondText("Housing 2 API default route reached :)")
                }

                get("/action") {
                    val uuid = call.request.queryParameters["uuid"] ?: return@get
                    val editing = editing[uuid]
                    if (editing == null) {
                        call.respondText("UUID not found", status = HttpStatusCode.NotFound)
                        return@get
                    }

                    var action = editing.second
                    if (action is HTSLImpl) {
                        call.respondText(action.export(0))
                    }
                }

                post("/action") {
                    val uuid = call.request.queryParameters["uuid"] ?: return@post
                    val secret = call.request.queryParameters["secret"] ?: return@post
                    val editing = editing[uuid]
                    if (editing == null) {
                        call.respondText("UUID not found", status = HttpStatusCode.NotFound)
                        return@post
                    }

                    if (editing.first != secret) {
                        call.respondText("Secret key is incorrect", status = HttpStatusCode.Unauthorized)
                        return@post
                    }

                    var action = editing.second
                    if (action is HTSLImpl) {
                        action.importAction(call.receiveText())
                    }
                }
            }
        }.start(wait = false)

        println("Housing2Api started on port ${Main.getInstance().config.getInt("api_port", 8080)}")
    }

    fun addEditing(uuid: String, secret: String, action: Action) {
        editing[uuid] = Duple(secret, action)
    }
}