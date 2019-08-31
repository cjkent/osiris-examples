package ws.osiris.example.thundra.core

import ws.osiris.aws.ApplicationConfig
import ws.osiris.aws.LambdaRuntime
import ws.osiris.aws.Stage
import java.time.Duration

/**
 * Configuration that controls how the application is deployed to AWS.
 */
val config = ApplicationConfig(
    applicationName = "osiris-thundra-example",
    lambdaName = "osiris-thundra-example",
    lambdaMemorySizeMb = 1024,
    lambdaTimeout = Duration.ofMinutes(1),
    runtime = LambdaRuntime.Provided,
    keepAliveCount = 1,
    layers = listOf(
        "arn:aws:lambda:\${AWS::Region}:269863060030:layer:thundra-lambda-java-layer:26"
    ),
    stages = listOf(
        Stage(
            name = "dev",
            description = "Development stage",
            deployOnUpdate = true
        )
    )
)
