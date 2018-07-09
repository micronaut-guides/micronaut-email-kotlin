package example.micronaut

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.simpleemail.model.Body
import com.amazonaws.services.simpleemail.model.Content
import com.amazonaws.services.simpleemail.model.Destination
import com.amazonaws.services.simpleemail.model.Message
import com.amazonaws.services.simpleemail.model.SendEmailRequest
import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton // <1>
@Requires(condition = AwsSesMailCondition::class) // <2>
@Primary // <3>
class AwsSesMailService(@Value("\${AWS_REGION:none}") awsRegionEnv: String,  // <4>
                        @Value("\${AWS_SOURCE_EMAIL:none}") sourceEmailEnv: String,
                        @Value("\${aws.region:none}") awsRegionProp: String,
                        @Value("\${aws.sourceemail:none}") sourceEmailProp: String,
                        private val awsCredentialsProviderService: AwsCredentialsProviderService?) : EmailService {

    private val awsRegion: String = if (awsRegionEnv != "none") awsRegionEnv else awsRegionProp
    private val sourceEmail: String = if (sourceEmailEnv != "none") sourceEmailEnv else sourceEmailProp

    private fun bodyOfEmail(email: Email): Body {
        if (email.htmlBody() != null && !email.htmlBody()!!.isEmpty()) {
            val htmlBody = Content().withData(email.htmlBody())
            return Body().withHtml(htmlBody)
        }
        if (email.textBody() != null && !email.textBody()!!.isEmpty()) {
            val textBody = Content().withData(email.textBody())
            return Body().withHtml(textBody)
        }
        return Body()
    }

    override fun send(email: Email) {

        if (awsCredentialsProviderService == null) {
            if (LOG.isWarnEnabled) {
                LOG.warn("AWS Credentials provider not configured")
            }
            return
        }

        var destination = Destination().withToAddresses(email.recipient())
        if (email.cc() != null) {
            destination = destination.withCcAddresses(email.cc())
        }
        if (email.bcc() != null) {
            destination = destination.withBccAddresses(email.bcc())
        }
        val subject = Content().withData(email.subject())
        val body = bodyOfEmail(email)
        val message = Message().withSubject(subject).withBody(body)

        var request = SendEmailRequest()
                .withSource(sourceEmail)
                .withDestination(destination)
                .withMessage(message)

        if (email.replyTo() != null) {
            request = request.withReplyToAddresses()
        }

        try {
            if (LOG.isInfoEnabled) {
                LOG.info("Attempting to send an email through Amazon SES by using the AWS SDK for Java...")
            }

            val client = AmazonSimpleEmailServiceClientBuilder.standard()
                    .withCredentials(awsCredentialsProviderService)
                    .withRegion(awsRegion)
                    .build()

            val sendEmailResult = client.sendEmail(request)

            if (LOG.isInfoEnabled) {
                LOG.info("Email sent! {}", sendEmailResult.toString())
            }
        } catch (ex: Exception) {
            if (LOG.isWarnEnabled) {
                LOG.warn("The email was not sent.")
                LOG.warn("Error message: {}", ex.message)
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(AwsSesMailService::class.java)
    }
}