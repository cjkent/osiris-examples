package io.github.cjkent.osiris.example.customauth1.core

import io.github.cjkent.osiris.aws.ApplicationConfig
import io.github.cjkent.osiris.aws.Stage
import java.time.Duration

/**
 * Configuration that controls how the application is deployed to AWS.
 */
val config = ApplicationConfig(
    applicationName = "osiris-custom-auth-example1",
    lambdaMemorySizeMb = 512,
    lambdaTimeout = Duration.ofSeconds(10),
    stages = listOf(
        Stage(
            name = "dev",
            description = "Development stage",
            deployOnUpdate = true
        )
    )
)
