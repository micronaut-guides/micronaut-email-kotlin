package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.exceptions.NoSuchBeanException
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertTrue

class SendGridEmailConditionSpec: Spek({

    describe("SendGridEmailService loaded if condition") {
        val applicationContext = ApplicationContext.run("test")
        it("Verify SendGridEmailService is NOT loaded if system properties or environment properties are not set") {
            var exceptionThrown = false
            try {
                applicationContext.getBean(SendGridEmailService::class.java)
            } catch (e: NoSuchBeanException) {
                exceptionThrown = true
            }
            assertTrue(exceptionThrown)
        }
        afterGroup {
            applicationContext.close()
        }
    }
})