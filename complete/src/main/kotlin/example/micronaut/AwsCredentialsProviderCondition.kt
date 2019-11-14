package example.micronaut

import io.micronaut.context.condition.Condition
import io.micronaut.context.condition.ConditionContext
import io.micronaut.core.util.StringUtils

class AwsCredentialsProviderCondition : Condition {

    override fun matches(context: ConditionContext<*>): Boolean {
        return envOrSystemProperty("AWS_ACCESS_KEY_ID", "aws.accesskeyid") &&
                envOrSystemProperty("AWS_SECRET_KEY", "aws.secretkey")
    }

    private fun envOrSystemProperty(env: String, prop: String): Boolean {
        return StringUtils.isNotEmpty(System.getProperty(prop)) || StringUtils.isNotEmpty(System.getenv(env))
    }
}
