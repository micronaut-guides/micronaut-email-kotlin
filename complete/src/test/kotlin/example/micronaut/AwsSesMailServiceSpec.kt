package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.exceptions.NoSuchBeanException
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertTrue

class AwsSesMailServiceSpec: Spek({

    describe("AwsSesMailService load") {
        val applicationContext = ApplicationContext.run("test")
        it("AwsSesMailService is not loaded if system property is not present") {
            var exceptionThrown = false
            try {
                applicationContext.getBean(AwsSesMailService::class.java)
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