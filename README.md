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

    docker build -t <docker id>/serviceb-<profile>:<mp version> -f src/main/profiles/liberty/Dockerfile .
    docker run -p 8080:8080 <docker id>/serviceb-<profile>:<mp version>
    e.g.
    docker build -t microprofile/serviceb-liberty:mp-3.0 -f src/main/profiles/liberty/Dockerfile .
    docker run -p 8080:8080 microprofile/serviceb-liberty:mp-3.0

The service will be accessible at http://localhost:8080/mp-servicemesh-sample/serviceB

## Thorntail

### Run the service locally

    mvn -Pthorntail package 
    java -jar target/service-b-thorntail.jar
    
or    
    
    mvn -Pthorntail thorntail:run -Dwildfly-swarm.useUberJar=true

The service will be accessible at http://localhost:8080/mp-servicemesh-sample/serviceB

### Run the service in a Docker container

<<<<<<< HEAD
    docker build -t &lt;docker id&gt;/serviceb-thorntail:&lt;tag id&gt; -f src/main/profiles/thorntail/Dockerfile .
    docker run -p 8080:8080 &lt;docker id&gt;/serviceb-thorntail:&lt;tag id&gt;
    e.g.
    docker build -t emijiang/serviceb-thorntail:mp-1.3 -f src/main/profiles/thorntail/Dockerfile .
    docker run -p 8080:8080 emijiang/serviceb-thorntail:mp-1.3
=======
    mvn -Pthorntail package 
    docker build -t <docker id>/serviceb-<profile>:<mp version> -f src/main/profiles/thorntail/Dockerfile .
    docker run -p 8080:8080 <docker id>/serviceb-<profile>:<mp version>
>>>>>>> 496ff07f60470ad439e954bf3c0d5c7316eac2be

The service will be accessible at http://localhost:8080/mp-servicemesh-sample/serviceB
