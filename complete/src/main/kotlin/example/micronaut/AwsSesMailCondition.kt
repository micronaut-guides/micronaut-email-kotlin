package example.micronaut

import io.micronaut.context.condition.Condition
import io.micronaut.context.condition.ConditionContext

class AwsSesMailCondition : Condition {

    override fun matches(context: ConditionContext<*>): Boolean {
        return envOrSysPropNotBlankAndNotNull("AWS_SOURCE_EMAIL", "aws.sourceemail")
        &&  envOrSysPropNotBlankAndNotNull("AWS_REGION", "aws.region")
    }

    private fun envOrSysPropNotBlankAndNotNull(env: String?, prop: String?): Boolean {
        return notBlankAndNotNull(System.getProperty(prop)) ||
                notBlankAndNotNull(System.getenv(env))
    }

    private fun notBlankAndNotNull(str: String?): Boolean {
        return str != null && str != ""
    }
}