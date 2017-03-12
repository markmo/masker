# Masker

Microservice to detect personally identifiable information (PII) in a text message.

The service returns a "pass = true" result if PII information is not detected, or
 "pass = false" otherwise.
 
To build this service for Docker, run

    sbt docker:publishLocal
    
To run the Docker service, run the provided bash script

    ./docker-run.sh

This executes

    docker run -p 49165:8080 -d masker:1.0

The service will be availble on port 49165

To attach to the shell of the running daemon

    docker exec -i -t <container-id> /bin/bash

The list of containers can be found using

    docker ps -a

From within the daemon shell, test the service using

    curl -H "content-Type: application/json" -X POST -d '{"text":"Moloney"}' http://localhost:8080/mask

From outside

    curl -H "content-Type: application/json" -X POST -d '{"text":"Moloney"}' http://localhost:49165/mask
    
As a convenience to stop and remove the Docker container and image

    ./docker-remove.sh <container-id>

Use Postman to test the API. Alternatively, you can use the Swagger UI at

    http://localhost:49165/swagger

Open the `mask` operation. Under Parameters, if you click in the yellow box under Data Type, the example value
is copied into the body value.

Change the `text` property to "Hello". Click on the small "Try it out!" button under the "Response Messages"
section. In the "Response Body" further below, a "pass = true" result should be shown. That is, the message
"Hello" does not contain any personally identifiable information.

Next, set the `text` property to your name. This time, when you click on "Try it out!", a "pass = false" result
should be shown.
