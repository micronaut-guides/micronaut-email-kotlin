package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.exceptions.NoSuchBeanException
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AwsCredentialsProviderConditionSpec: Spek({

    describe("AwsCredentialsProviderService loaded if condition") {
        val applicationContext = ApplicationContext.run("test")
        on("Verify AwsCredentialsProviderService is NOT loaded if system properties or environment properties are not set") {
            var exceptionThrown = false
            try {
                applicationContext.getBean(AwsCredentialsProviderService::class.java)
            } catch (e: NoSuchBeanException) {
                exceptionThrown = true
            }
            assertTrue(exceptionThrown)
        }
//        on("Verify AwsCredentialsProviderService loaded if system properties are set") {
//            System.setProperty("aws.accesskeyid", "XXXX")
//            System.setProperty("aws.secretkey", "YYYY")
//            var exceptionThrown = false
//            try {
//                val awsCredentialsProviderService = applicationContext.getBean(AwsCredentialsProviderService::class.java)
//                assertTrue(awsCredentialsProviderService.secretKey == "YYYY")
//                assertTrue(awsCredentialsProviderService.accessKey == "XXXX")
//            } catch (e: NoSuchBeanException) {
//                exceptionThrown = true
//            }
//            assertFalse(exceptionThrown)
//
//            System.setProperty("aws.accesskeyid", null)
//            System.setProperty("aws.secretkey", null)
//        }
        afterGroup {
            applicationContext.close()
        }
    }
})