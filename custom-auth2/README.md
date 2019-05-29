# Custom Authorisation

Custom authorisation is one of the mechanisms available in API Gateway for controlling access to API endpoints. See [here](http://docs.aws.amazon.com/apigateway/latest/developerguide/use-custom-authorizer.html) for details.

When a request is received for an endpoint protected by custom authorisation, a lambda function is invoked to authenticate the caller. If the caller is permitted to invoke the endpoint the lambda function returns an IAM policy document with the required permissions.

Custom authorisation can be used to integrate third-party authentication systems with API Gateway, for example Auth0.

There are two example projects showing custom authorisation:

## Custom Authorisation 2

This project defines an API with custom authorisation but does not define an authorisation lambda. The lambda is assumed to be defined independently of the project containing the REST API.

In order to integrate the custom authorisation lambda with the API, the ARN of the lambda must be specified in the Osiris application configuration. The property name is `authConfig` and it must be populated with an instance of `AuthConfig.Custom`. See [here](https://github.com/cjkent/osiris-examples/blob/master/custom-auth2/core/src/main/kotlin/io/github/cjkent/osiris/example/customauth2/core/Config.kt) for an example. The configuration in the example project includes a non-functional ARN that must be replaced with the ARN of a real authorisation lambda before the project will work.
