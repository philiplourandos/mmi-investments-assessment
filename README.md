# mmi-investments-assessment

## Setup

- On linux or mac install [http://sdkman.io](SDKMan). Once installed open a terminal and run: `sdk env install`.
- docker or podman is required as test cases use a Postgres container image

## Project Structure

The root directory of this project contains the parent pom to build:

- `assessment-openapi` - This contains the openapi documentation of the endpoints. A client side jar is built from the api
- `assessment-service` - This is the actual service

`mvn clean install` can be used to built all the modules