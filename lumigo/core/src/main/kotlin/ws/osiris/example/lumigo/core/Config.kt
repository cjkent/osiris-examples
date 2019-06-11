package ws.osiris.example.lumigo.core

import ws.osiris.aws.ApplicationConfig
import ws.osiris.aws.Stage
import java.time.Duration

/**
 * Configuration that controls how the application is deployed to AWS.
 */
val config = ApplicationConfig(
    applicationName = "osiris-lumigo-example",
    lambdaName = "osiris-lumigo-example",
    lambdaMemorySizeMb = 1024,
    lambdaTimeout = Duration.ofMinutes(1),
    keepAliveCount = 1,
    stages = listOf(
        Stage(
            name = "dev",
            description = "Development stage",
            deployOnUpdate = true
        )
    )
)
