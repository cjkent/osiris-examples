package ws.osiris.example.customauth2.core

import ws.osiris.aws.ApplicationConfig
import ws.osiris.aws.AuthConfig
import ws.osiris.aws.Stage
import java.time.Duration

/**
 * Configuration that controls how the application is deployed to AWS.
 */
val config = ApplicationConfig(
    applicationName = "osiris-custom-auth-example2",
    lambdaMemorySizeMb = 512,
    lambdaTimeout = Duration.ofSeconds(10),
    // TODO This must be the ARN of a custom authorisation lambda defined manually or in a different stack
    authConfig = AuthConfig.Custom("arn:aws:lambda:[region]:[account]:function:[function-name]"),
    stages = listOf(
        Stage(
            name = "dev",
            description = "Development stage",
            deployOnUpdate = true
        )
    )
)
