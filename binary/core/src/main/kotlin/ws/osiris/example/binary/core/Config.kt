package ws.osiris.example.binary.core

import ws.osiris.aws.ApplicationConfig
import ws.osiris.aws.Stage
import java.time.Duration

/**
 * Configuration that controls how the application is deployed to AWS.
 */
val config = ApplicationConfig(
    applicationName = "osiris-binary-example",
    lambdaMemorySizeMb = 512,
    lambdaTimeout = Duration.ofSeconds(10),
    environmentVariables = mapOf(
        "EXAMPLE_ENVIRONMENT_VARIABLE" to "Bob"
    ),
    binaryMimeTypes = setOf(
        "image/png",
        "image/jpeg"
    ),
    stages = listOf(
        Stage(
            name = "dev",
            description = "Development stage",
            deployOnUpdate = true,
            variables = mapOf(
                "VAR1" to "devValue1",
                "VAR2" to "devValue2"
            )
        ),
        Stage(
            name = "prod",
            description = "Production stage",
            deployOnUpdate = false,
            variables = mapOf(
                "VAR1" to "prodValue1",
                "VAR2" to "prodValue2"
            )
        )
    )
)
