package example.micronaut

import io.micronaut.context.condition.Condition
import io.micronaut.context.condition.ConditionContext

class SendGridEmailCondition : Condition {
    override fun matches(context: ConditionContext<*>?): Boolean {
        return envOrSysPropNotBlankAndNotNull("SENDGRID_APIKEY", "sendgrid.apikey")
                &&  envOrSysPropNotBlankAndNotNull("SENDGRID_FROM_EMAIL", "sendgrid.fromemail")
    }

    private fun envOrSysPropNotBlankAndNotNull(env: String?, prop: String?): Boolean {
        return notBlankAndNotNull(System.getProperty(prop)) ||
                notBlankAndNotNull(System.getenv(env))
    }

    private fun notBlankAndNotNull(str: String?): Boolean {
        return str != null && str != ""
    }

}