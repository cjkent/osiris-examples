# CORS
This example show how to enable Cross-Origin Resource Sharing (CORS) in an API.

The API has a flag `cors = true` indicating that the Osiris CORS support should be enabled for all endpoints in the API. The `cors` block defines the headers that should be added to the response for every CORS-enabled endpoint.

The file `web/index.html` is a web page containing a very simple script that makes CORS requests to the API. In order to run the example you need to edit this file with the ID of your API and the ID of the AWS region in which it is hosted.

When the API has been deployed and the page has been edited, open the page in a browser. 

Pressing the button labelled "Simple Request" makes a simple CORS request to the API. The browser makes a single `GET` request, and the response contains the header `Access-Control-Allow-Origin: *`. This tells the browser that the API accepts cross-origin requests from any origin. 

Pressing the button labelled "Preflighted Request" makes a `POST` request to the API with a `Content-Type` of `application/json`. This means that the browser must make a pre-flight `OPTIONS` request to check that the cross-origin request is allowed. The response to the `OPTIONS` request contains the required CORS headers, and the browser makes the `POST` request. The "Network" tab in the browser developer tool can be used to view the two requests that the browser makes when the scripts makes a single `POST` request.

For more details about CORS support in Osiris see [this page](https://github.com/cjkent/osiris/wiki/CORS) in the Osiris wiki. For a comprehensive explanation of CORS see the [Mozilla Documentation](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS).
