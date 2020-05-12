# Cognito
This example shows how to integrate Osiris with [AWS Cognito](https://aws.amazon.com/cognito/) in order to sign up and authenticate users. Cognito is used to authenticate users, and the [Cognito Hosted UI](https://docs.aws.amazon.com/cognito/latest/developerguide/cognito-user-pools-app-integration.html) is used to allow users to sign up and sign in.

## API Definition
The endpoint in the API is in an `auth` block specifying `CognitoUserPoolsAuth`. This means that API Gateway verifies that the `Authenitcation` header contains a token issued by the Cognito User Pool associated with the API. If the request contains a valid token it is allowed to proceed, otherwise a status of 403 (Forbidden) is returned before the request reaches the Osiris code.

The API definition contains a `filter` that extracts the user information from the token. The token is a JSON Web Token (JWT) encoded as a Base64 string. The filter converts it to text, parses the JSON into a map and extracts the fields containing the user email and their Cognito ID.

## CloudFormation Template
The template file `root.template` defines three Cognito resources:

* `UserPool` - the Cognito user pool that contains the users
* `UserPoolClient` - the Cognito user pool client used to authenticate users when they sign in
* `UserPoolDomain` - sets up the domain of the Cognito Hosted UI where users sign up and sign in

## HTML Page
The file `index.html` is an HTML page that contains code to handle Cognito authentication and to make a request to the API after authentication using the token.

The page contains two pieces of logic:

1) When the page loads it checks for the presence of `id_token` in the URL hash. If it is not found then the browser is redirected to the Cognito Hosted UI where the user can sign up or sign in.

2) When the button is clicked the ID token is parsed from the URL hash. Then a request is made to the API with the ID token in the `Authorization` header. The content of the response is displayed on the page, including details about the logged-in user.

## Running the Example
The example must be deployed in two steps. First, deploy using `mvn deploy` as usual. Then edit the script in `index.html`, specifying your AWS account ID and region, the ID of the `OsirisExampleClient` Cognito client and the ID of your API Gateway API. These values can all be found in the AWS console.

Then deploy again, and run this in a terminal:

```
mvn osiris:open -f core -Dstage=dev -Dendpoint=/
```

This will open the index page in a browser. You should be redirected to the Cognito Hosted UI where you should sign up as a new user. You will be sent a verification email containing a code. Enter this code into the Cognito UI, verify your user and sign in.

You should now see the page. It contains a single button. When you press the button a request is made to the UI, passing the token provided by Cognito. The response will be shown, containing your email address and the ID of your user in the Cognito user pool.
