package example.micronaut

import io.micronaut.context.condition.Condition
import io.micronaut.context.condition.ConditionContext

class AwsCredentialsProviderCondition : Condition {

    override fun matches(context: ConditionContext<*>): Boolean {
        return envOrSystemProperty("AWS_ACCESS_KEY_ID", "aws.accesskeyid") &&
                envOrSystemProperty("AWS_SECRET_KEY", "aws.secretkey")
    }

    private fun envOrSystemProperty(env: String, prop: String): Boolean {
        return notBlankAndNotNull(System.getProperty(prop)) || notBlankAndNotNull(System.getenv(env))
    }

    private fun notBlankAndNotNull(str: String?): Boolean {
        return str != null && str != ""
    }
}