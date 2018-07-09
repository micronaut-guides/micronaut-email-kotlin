package example.micronaut

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import io.micronaut.context.annotation.Requires
import javax.inject.Singleton
import com.amazonaws.auth.BasicAWSCredentials
import io.micronaut.context.annotation.Value

@Singleton
@Requires(condition = AwsCredentialsProviderCondition::class)
class AwsCredentialsProviderService(@param:Value("\${AWS_ACCESS_KEY_ID:none}") val accessKeyEnv : String,
                                    @param:Value("\${AWS_SECRET_KEY:none}") val secretKeyEnv : String,
                                    @param:Value("\${aws.accesskeyid:none}") val accessKeyProp : String,
                                    @param:Value("\${aws.secretkey:none}") val secretKeyProp : String) : AWSCredentialsProvider {
    private val accessKey : String
    private val secretKey : String

    init {
        if (accessKeyEnv != "none") {
            accessKey = accessKeyEnv
        } else {
            accessKey = accessKeyProp
        }
        if (secretKeyEnv != "none") {
            secretKey = accessKeyEnv
        } else {
            secretKey = secretKeyProp
        }
    }
    override fun refresh() {
        TODO("not implemented")
    }

    override fun getCredentials(): AWSCredentials {
        return BasicAWSCredentials(accessKey, secretKey)
    }
}