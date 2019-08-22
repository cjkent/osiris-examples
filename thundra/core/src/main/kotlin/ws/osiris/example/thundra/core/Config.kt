package ws.osiris.example.thundra.core

import ws.osiris.aws.ApplicationConfig
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
    keepAliveCount = 1,
    stages = listOf(
        Stage(
            name = "dev",
            description = "Development stage",
            deployOnUpdate = true
        )
    ),
    bucketSuffix = "osiris-thundra-example"
)
