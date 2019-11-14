package example.micronaut

import io.micronaut.context.condition.Condition
import io.micronaut.context.condition.ConditionContext
import io.micronaut.core.util.StringUtils

class SendGridEmailCondition : Condition {
    override fun matches(context: ConditionContext<*>?): Boolean {
        return envOrSysPropNotBlankAndNotNull("SENDGRID_APIKEY", "sendgrid.apikey")
                && envOrSysPropNotBlankAndNotNull("SENDGRID_FROM_EMAIL", "sendgrid.fromemail")
    }

    private fun envOrSysPropNotBlankAndNotNull(env: String?, prop: String?): Boolean {
        return StringUtils.isNotEmpty(System.getProperty(prop)) || StringUtils.isNotEmpty(System.getenv(env))
    }
}
