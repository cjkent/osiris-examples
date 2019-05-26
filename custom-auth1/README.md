# Custom Authorisation

Custom authorisation is one of the mechanisms available in API Gateway for controlling access to API endpoints. See [here](http://docs.aws.amazon.com/apigateway/latest/developerguide/use-custom-authorizer.html) for details.

When a request is received for an endpoint protected by custom authorisation, a lambda function is invoked to authenticate the caller. If the caller is permitted to invoke the endpoint the lambda function returns an IAM policy document with the required permissions.

Custom authorisation can be used to integrate third-party authentication systems with API Gateway, for example Auth0.

There are two example projects showing custom authorisation:

## Custom Authorisation 1

This project defines and deploys the custom authorisation lambda as part of the same project as the API. The lambda class is `CustomAuthorizer` and it is defined in the the file `ApiDefinition.kt` (the same file as the API).

The CloudFormation resource defining the authorisation lambda is included in the file `root.template` and is called `CustomAuthFunction`. It is deployed in the same CloudFormation stack as the API Gateway API and the lambda that handles requests to the API.

See [this page](https://github.com/cjkent/osiris/wiki/Creating-AWS-Resources) for details of how to create other AWS resources in an Osiris project.

The authorisation logic in `CustomAuthorizer` is trivial - it checks whether the authorisation token passed in the `Authorization` header is the string "allow". If so it returns an IAM policy document with permission to invoke the endpoint. A real authorisation lambda function would validate the authorisation token before returning the policy.
