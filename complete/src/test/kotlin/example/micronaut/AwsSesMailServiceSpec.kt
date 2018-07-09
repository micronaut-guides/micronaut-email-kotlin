package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.exceptions.NoSuchBeanException
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AwsSesMailServiceSpec: Spek({

    describe("AwsSesMailService load") {
        val applicationContext = ApplicationContext.run("test")
        on("AwsSesMailService is not loaded if system property is not present") {
            var exceptionThrown = false
            try {
                applicationContext.getBean(AwsSesMailService::class.java)
            } catch (e: NoSuchBeanException) {
                exceptionThrown = true
            }
            assertTrue(exceptionThrown)
        }
//        on("Verify AwsSesMailService loaded if system properties are set") {
//        System.setProperty("aws.region", "XXXX")
//        System.setProperty("aws.sourceemail", "me@micronaut.example")
//        System.setProperty("aws.accesskeyid", "XXXX")
//        System.setProperty("aws.secretkey", "YYYY")
//            var exceptionThrown = false
//            try {
//                applicationContext.getBean(AwsSesMailService::class.java)
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