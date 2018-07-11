# MicroProfile Service Mesh Sample Service B
This is the serviceB component of the microprofile service mesh sample.

## Requirements
* [Docker](https://www.docker.com/)
* [Maven](https://maven.apache.org/install.html)
* [Java 8]: Any compliant JDK should work.
  * [Java 8 JDK from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
  * [Java 8 JDK from IBM (AIX, Linux, z/OS, IBM i)](http://www.ibm.com/developerworks/java/jdk/),
    or [Download a Liberty server package](https://developer.ibm.com/assets/wasdev/#filter/assetTypeFilters=PRODUCT)
    that contains the IBM JDK (Windows, Linux)

## Build

    mvn install

## Package and run the service

The pom is designed to contain application server profiles with which you can test and run the service. Currently the *liberty* profile is provided.

## Open Liberty

### Run the service locally

    mvn -P liberty install liberty:run-server

The service will be accessible at http://localhost:8080/mp-servicemesh-sample/serviceB

### Run the service locally in a Docker container

    docker build -t serviceb -f src/main/profiles/liberty/Dockerfile .
    docker run -p 8080:8080 serviceb

The service will be accessible at http://localhost:8080/mp-servicemesh-sample/serviceB
