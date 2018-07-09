package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.exceptions.NoSuchBeanException
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SendGridEmailConditionSpec: Spek({

    describe("SendGridEmailService loaded if condition") {
        val applicationContext = ApplicationContext.run("test")
        on("Verify SendGridEmailService is NOT loaded if system properties or environment properties are not set") {
            var exceptionThrown = false
            try {
                applicationContext.getBean(SendGridEmailService::class.java)
            } catch (e: NoSuchBeanException) {
                exceptionThrown = true
            }
            assertTrue(exceptionThrown)
        }
//        on("Verify SendGridEmailService loaded if system properties are set") {
//        System.setProperty("sendgrid.apikey", "XXXX")
//        System.setProperty("sendgrid.fromemail", "me@micronaut.example")
//            var exceptionThrown = false
//            try {
//                applicationContext.getBean(SendGridEmailService::class.java)
//
//            } catch (e: NoSuchBeanException) {
//                exceptionThrown = true
//            }
//            assertFalse(exceptionThrown)
//        }
        afterGroup {
            applicationContext.close()
        }
    }
})