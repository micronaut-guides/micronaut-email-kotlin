package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertTrue

class MailControllerValidationSpec: Spek({

    describe("MailController Validation") {
        var embeddedServer : EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java, mapOf("spec.name" to "mailcontroller"),
                "test")  // <1>
        var client : RxHttpClient = RxHttpClient.create(embeddedServer.url)  // <2>

        it("/mail/send cannot be invoked without subject") {
            val cmd = EmailCmd()
            cmd.recipient = "delamos@micronaut.example"
            cmd.textBody = "Hola hola"

            val request = HttpRequest.POST("/mail/send", cmd) // <3>

            var exceptionThrown = false
            try {
                client.toBlocking().exchange(request, HttpResponse::class.java)
            } catch (e: HttpClientResponseException) {
                exceptionThrown = true
            }
            assertTrue(exceptionThrown)
        }

        it("/mail/send cannot be invoked without recipient") {
            val cmd = EmailCmd()
            cmd.subject = "Hola"
            cmd.textBody = "Hola hola"
            val request = HttpRequest.POST("/mail/send", cmd) // <3>

            var exceptionThrown = false
            try {
                client.toBlocking().exchange(request, HttpResponse::class.java)
            } catch (e: HttpClientResponseException) {
                exceptionThrown = true
            }
            assertTrue(exceptionThrown)
        }

        it("/mail/send cannot be invoked without either textBody or htmlBody") {
            val cmd = EmailCmd()
            cmd.subject = "Hola"
            cmd.recipient = "delamos@micronaut.example"
            val request = HttpRequest.POST("/mail/send", cmd) // <3>

            var exceptionThrown = false
            try {
                client.toBlocking().exchange(request, HttpResponse::class.java)
            } catch (e: HttpClientResponseException) {
                exceptionThrown = true
            }
            assertTrue(exceptionThrown)
        }

        afterGroup {
            client.close()
            embeddedServer.close()
        }
    }
})
