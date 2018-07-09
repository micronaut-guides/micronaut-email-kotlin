package example.micronaut

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@EmailConstraints
class EmailCmd : Email {

    //tag::properties[]
    @NotBlank
    @NotNull
    val recipient: String? = null

    @NotBlank
    @NotNull
    val subject: String? = null

    val cc: List<String>? = null

    val bcc: List<String>? = null

    val htmlBody: String? = null

    val textBody: String? = null

    val replyTo: String? = null
    //end::properties[]

    //tag::settersandgetters[]
    override fun recipient(): String? {
        return this.recipient
    }

    override fun cc(): List<String>? {
        return this.cc
    }

    override fun bcc(): List<String>? {
        return this.bcc
    }

    override fun subject(): String? {
        return this.subject
    }

    override fun htmlBody(): String? {
        return this.htmlBody
    }

    override fun textBody(): String? {
        return this.textBody
    }

    override fun replyTo(): String? {
        return this.replyTo
    }
    //end::settersandgetters[]
}
