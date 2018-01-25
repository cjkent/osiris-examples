# Custom Authorisation

Custom authorisation is one of the mechanisms available in API Gateway for controlling access to API endpoints. See [here](http://docs.aws.amazon.com/apigateway/latest/developerguide/use-custom-authorizer.html) for details.

When a request is received for an endpoint protected by custom authorisation, a lambda function is invoked to authenticate the caller. If the caller is permitted to invoke the endpoint the lambda function returns an IAM policy document with the required permissions.

Custom authorisation can be used to integrate third-party authentication systems with API Gateway, for example Auth0.

There are two example projects showing custom authorisation:

## Custom Authorisation 1

This project defines and deploys the custom authorisation lambda as part of the same project as the API. The lambda class is `CustomAuthorizer` and it is defined in the the file `ApiDefinition.kt` (the same file as the API).

The CloudFormation resource defining the authorisation lambda is included in the file `root.template` and is called `CustomAuthFunction`. It is deployed in the same CloudFormation stack as the API Gateway API and the lambda that handles requests to the API.

TODO link to the wiki page describing how to define additional AWS resources (once it has been written).

The authorisation logic in `CustomAuthorizer` is trivial - it checks whether the authorisation token passed in the `Authorization` header is the string "allow". If so it returns an IAM policy document with permission to invoke the endpoint. A real authorisation lambda function would validate the authorisation token before returning the policy.

## Custom Authorisation 2

This project defines an API with custom authorisation but does not define an authorisation lambda. The lambda is assumed to be defined independently of the project containing the REST API.

In order to integrate the custom authorisation lambda with the API, the ARN of the lambda must be specified in the Osiris application configuration. The property name is `authConfig` and it must be populated with an instance of `AuthConfig.Custom`. See [here](https://github.com/cjkent/osiris-examples/blob/master/custom-auth/custom-auth2/core/src/main/kotlin/io/github/cjkent/osiris/example/customauth2/core/Config.kt) for an example. The configuration in the example project includes a non-functional ARN that must be replaced with the ARN of a real authorisation lambda before the project will work.
