# Osiris Examples
Example projects built with [Osiris](https://github.com/cjkent/osiris/).

* [Java](https://github.com/cjkent/osiris-examples/tree/master/java) - An application where all the logic is written in Java except the API definition.
* [DynamoDB](https://github.com/cjkent/osiris-examples/tree/master/dynamodb) - An application that uses AWS DynamoDB as its data store. Demonstrates how to create and refer to other AWS resources from an Osiris app.
* [Custom Auth](https://github.com/cjkent/osiris-examples/tree/master/custom-auth) - Two projects showing how custom authorisation logic can be used to control access to API endpoints.
  * [Custom Auth 1](https://github.com/cjkent/osiris-examples/tree/master/custom-auth/custom-auth1) - The custom authorisation lambda is defined and deployed as part of the same project as the API.
  * [Custom Auth 2](https://github.com/cjkent/osiris-examples/tree/master/custom-auth/custom-auth2) - The custom authorisation lambda is assumed to be defined separately. The ARN of the custom authorisation lambda must be specified in the configuration of the Osiris Maven plugin.
* [Cognito](https://github.com/cjkent/osiris-examples/tree/master/cognito) - An application that uses Cognito for auth
* [Binary](https://github.com/cjkent/osiris-examples/tree/master/binary) - An application that uses binary MIME types.
* [Lumigo](https://github.com/cjkent/osiris-examples/tree/master/lumigo) - An application that integrates with the [Lumigo](https://lumigo.io/) monitoring platform.

See the individual project directories for a detailed description.
