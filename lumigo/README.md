# Lumigo Example Project

This project demonstrates integration with the Lumigo monitoring platform (https://lumigo.io).

A filter is defined in `ApiDefinition.kt` that is applied to all requests. The filter handles the request by calling `LumigoRequestExecutor.execute()`. It passes in the event and context that were passed to the Osiris lambda by AWS. It also passes in an anonymous function that invokes the handler with the request. This dispatches the request to the appropriate handler.
