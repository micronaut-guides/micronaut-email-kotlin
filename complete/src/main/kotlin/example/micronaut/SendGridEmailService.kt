package example.micronaut

import com.sendgrid.Content
import com.sendgrid.Mail
import com.sendgrid.Method
import com.sendgrid.Personalization
import com.sendgrid.Request
import com.sendgrid.SendGrid
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import java.io.IOException

@Singleton
@Requires(condition = SendGridEmailCondition::class)
class SendGridEmailService(@Value("\${SENDGRID_APIKEY:none}") apiKeyEnv: String,
                                    @Value("\${SENDGRID_FROM_EMAIL:none}") fromEmailEnv: String,
                                    @Value("\${sendgrid.apikey:none}") apiKeyProp: String,
                                    @Value("\${sendgrid.fromemail:none}") fromEmailProp: String) : EmailService {
    protected val apiKey: String
    protected val fromEmail: String

    init {
        this.apiKey = if (apiKeyEnv != "none") apiKeyEnv else apiKeyProp
        this.fromEmail = if (fromEmailEnv != "none") fromEmailEnv else fromEmailProp
    }

    protected fun contentOfEmail(email: Email): Content? {
        if (email.textBody() != null) {
            return Content("text/plain", email.textBody())
        }
        return if (email.htmlBody() != null) {
            Content("text/html", email.htmlBody())
        } else null
    }

    override fun send(email: Email) {

        val personalization = Personalization()
        personalization.subject = email.subject()

        val to = com.sendgrid.Email(email.recipient())
        personalization.addTo(to)

        if (email.cc() != null) {
            for (cc in email.cc()!!) {
                val ccEmail = com.sendgrid.Email()
                ccEmail.email = cc
                personalization.addCc(ccEmail)
            }
        }

        if (email.bcc() != null) {
            for (bcc in email.bcc()!!) {
                val bccEmail = com.sendgrid.Email()
                bccEmail.email = bcc
                personalization.addBcc(bccEmail)
            }
        }

        val mail = Mail()
        val from = com.sendgrid.Email()
        from.email = fromEmail
        mail.from = from
        mail.addPersonalization(personalization)
        val content = contentOfEmail(email)
        mail.addContent(content!!)

        val sg = SendGrid(apiKey)
        val request = Request()
        try {
            request.method = Method.POST
            request.endpoint = "mail/send"
            request.body = mail.build()

            val response = sg.api(request)
            if (LOG.isInfoEnabled) {
                LOG.info("Status Code: {}", response.statusCode.toString())
                LOG.info("Body: {}", response.body)
                for (k in response.headers.keys) {
                    val v = response.headers[k]
                    LOG.info("Response Header {} => {}", k, v)
                }
            }


        } catch (ex: IOException) {
            if (LOG.isErrorEnabled) {
                LOG.error(ex.message)
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SendGridEmailService::class.java)
    }
}