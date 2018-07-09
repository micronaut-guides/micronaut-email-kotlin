package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.validation.Valid

@Controller("/mail") // <1>
@Validated // <2>
open class MailController(private val emailService: EmailService) {  // <3>

    @Post("/send") // <4>
    open fun send(@Body @Valid cmd: EmailCmd): HttpResponse<*> {  // <5>
        emailService.send(cmd)
        return HttpResponse.ok<Any>() // <6>
    }
}