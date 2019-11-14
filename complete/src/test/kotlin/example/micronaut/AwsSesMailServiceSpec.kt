package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.exceptions.NoSuchBeanException
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertTrue

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