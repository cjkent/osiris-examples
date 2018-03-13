package ws.osiris.example.resources.core

import com.amazonaws.services.lambda.runtime.ClientContext
import com.amazonaws.services.lambda.runtime.CognitoIdentity
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger

internal object FakeContext : Context {
    override fun getAwsRequestId(): String {
        throw UnsupportedOperationException("getAwsRequestId not implemented")
    }

    override fun getLogStreamName(): String {
        throw UnsupportedOperationException("getLogStreamName not implemented")
    }

    override fun getClientContext(): ClientContext {
        throw UnsupportedOperationException("getClientContext not implemented")
    }

    override fun getFunctionName(): String {
        throw UnsupportedOperationException("getFunctionName not implemented")
    }

    override fun getRemainingTimeInMillis(): Int {
        throw UnsupportedOperationException("getRemainingTimeInMillis not implemented")
    }

    override fun getLogger(): LambdaLogger {
        throw UnsupportedOperationException("getLogger not implemented")
    }

    override fun getInvokedFunctionArn(): String {
        throw UnsupportedOperationException("getInvokedFunctionArn not implemented")
    }

    override fun getMemoryLimitInMB(): Int {
        throw UnsupportedOperationException("getMemoryLimitInMB not implemented")
    }

    override fun getLogGroupName(): String {
        throw UnsupportedOperationException("getLogGroupName not implemented")
    }

    override fun getFunctionVersion(): String {
        throw UnsupportedOperationException("getFunctionVersion not implemented")
    }

    override fun getIdentity(): CognitoIdentity {
        throw UnsupportedOperationException("getIdentity not implemented")
    }
}
