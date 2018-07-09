package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import io.reactivex.Flowable
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MailControllerSpec: Spek({

    describe("MailController") {

        var embeddedServer : EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java, mapOf("spec.name" to "mailcontroller"),
                "test")  // <1>
        var client : RxHttpClient = RxHttpClient.create(embeddedServer.url)  // <2>

        on("/mail/send interacts once email service") {
            val cmd = EmailCmd()
            cmd.subject = "Test"
            cmd.recipient = "delamos@grails.example"
            cmd.textBody = "Hola hola"

            val request = HttpRequest.POST("/mail/send", cmd) // <3>

            var emailServices = embeddedServer.applicationContext.getBeansOfType(EmailService::class.java)

            assertTrue(emailServices.size == 1)

            var emailService = embeddedServer.applicationContext.getBean(EmailService::class.java)

            assertTrue(emailService is MockEmailService)

            val oldcount : Int  = (emailService as MockEmailService).emails.size

            val rsp : HttpResponse<Any> = client.toBlocking().exchange(request)

            assertEquals(rsp.status, HttpStatus.OK)

            val count : Int  = (emailService as MockEmailService).emails.size
            assertEquals(count, oldcount + 1) // <4>
        }

        afterGroup {
            client.close()
            embeddedServer.close()
        }

    }
})