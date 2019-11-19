package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MailControllerSpec : Spek({

    describe("MailController") {

        var embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java, mapOf("spec.name" to "mailcontroller"),
                "test")  // <1>
        var client: RxHttpClient = RxHttpClient.create(embeddedServer.url)  // <2>

        it("/mail/send interacts once email service") {
            val cmd = EmailCmd()
            cmd.subject = "Test"
            cmd.recipient = "delamos@grails.example"
            cmd.textBody = "Hola hola"

            val request = HttpRequest.POST("/mail/send", cmd) // <3>

            var emailServices = embeddedServer.applicationContext.getBeansOfType(EmailService::class.java)

            assertTrue(emailServices.size == 1)

            var emailService = embeddedServer.applicationContext.getBean(EmailService::class.java)

            assertTrue(emailService is MockEmailService)

            val oldcount: Int = (emailService as MockEmailService).emails.size

            val rsp: HttpResponse<Any> = client.toBlocking().exchange(request)

            assertEquals(HttpStatus.OK, rsp.status)

            val count: Int = (emailService as MockEmailService).emails.size
            assertEquals(oldcount + 1, count) // <4>
        }

        afterGroup {
            client.close()
            embeddedServer.close()
        }

    }
})