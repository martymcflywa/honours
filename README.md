# CSI3344: Honours RMI

![Travis (.org)](https://img.shields.io/travis/martymcflywa/honours.svg?) ![Coveralls github](https://img.shields.io/coveralls/github/martymcflywa/honours.svg) ![Codacy grade](https://img.shields.io/codacy/grade/220174f7b8e746189207af9988f7ce0b.svg)

Martin Ponce 10371381  
Monday 20th May 2019

- [CSI3344: Honours RMI](#csi3344-honours-rmi)
  - [Executive summary](#executive-summary)
  - [Introduction](#introduction)
    - [Introduction to RMI](#introduction-to-rmi)
    - [Application overview](#application-overview)
  - [Requirements](#requirements)
  - [DevOps](#devops)
  - [Design and implementation](#design-and-implementation)
  - [Setup and usage](#setup-and-usage)
    - [Bash script](#bash-script)
    - [Manual invocation](#manual-invocation)
  - [Operation examples](#operation-examples)
    - [New student and course](#new-student-and-course)
    - [Existing student and course](#existing-student-and-course)
    - [Input validation examples](#input-validation-examples)
  - [Conclusion](#conclusion)
  - [References](#references)

## Executive summary

## Introduction

### Introduction to RMI

### Application overview

The purpose of the application is to assess a student's university course marks, to determine whether or not the student is eligible for honours study.

The application uses a distributed, three tiered approach:

1. Client: provides a commandline user interface
2. Server: performs assessment and business logic
3. Persistence: recalls/stores data to/from the server

Communication between each tier is handled via RMI.

## Requirements

- JDK 1.8+
- Maven 3.6.0

## DevOps

- Version control: [github](https://github.com/martymcflywa/honours)
- CI/CD: [travis-ci](https://travis-ci.org/martymcflywa/honours)
- Code coverage: [coveralls](https://coveralls.io/github/martymcflywa/honours)
- Static analysis: [codacy](https://app.codacy.com/project/martymcflywa/honours/dashboard)

## Design and implementation

- `au.com.martinponce.honours.interfaces`
  - Contains remote interfaces and common abstractions between all three tiers
- `au.com.martinponce.honours.core`
  - Contains implementations of common abstractions that are shared between all three tiers
  - It is a shared dependency
- `au.com.martinponce.honours.persist.hibernate`
  - Implementation of persistence interface
  - Interacts with a mysql database using [hibernate](https://hibernate.org/) ORM
- `au.com.martinponce.honours.persist`
  - Bootstrapper for any persistence implementation
  - Currently uses the hibernate implementation above
  - But could be switched to any implementation if required
- `au.com.martinponce.honours.server`
  - Contains implementations of remote interfaces for
    - Business logic and rules relating to honours assessments
    - Authentication
  - Interacts with the persistence layer via RMI calls
- `au.com.martinponce.honours.client`
  - Contains the user interface application, interacts with the server via RMI calls
  - Commandline interface

## Setup and usage

### Bash script

See `./scripts/start-honours.sh`. In order to use the script, the following prerequisites are required:

- Bash shell
- Java installed and bin is accessible from path
- Maven installed and bin is accessible from path

The script conveniently wraps the build, and execution of the applications. Maven is invoked first, to build the project and its modules, and packages each assembly into its own self-contained executable jar.

Once the build is complete, `rmiregistry` is started as a background process, so that any servers can register their objects to the service and make them available to clients. The persistence service is then started as a background process, as well as the server, then the client in the foreground.

If the client is shutdown gracefully, the script will also shut down persistence and server background processes.

### Manual invocation

1. Build the project using maven, skipping unit tests
  - `mvn clean package -Dmaven.test.skip=true`
2. Set the remote interfaces package to the `CLASSPATH` environment variable
  - Windows: `set CLASSPATH .\honours-interfaces\target\honours-interfaces-2.0.0-SNAPSHOT.jar`
  - Linux/MacOS: `CLASSPATH=./honours-interfaces/target/honours-interfaces-2.0.0-SNAPSHOT.jar`
3. Execute the persistence service as background process (or in a separate console)
  - `java -jar -Djava.security.policy=security.policy ./honours-persist/target/honours-persist-2.0.0-SNAPSHOT-jar-with-dependencies.jar &`
3. Execute the server in the server as a background process (or in a separate console)
  - `java -jar -Djava.security.policy=security.policy ./honours-server/target/honours-server-2.0.0-SNAPSHOT-jar-with-dependencies.jar &`
4. Execute the client in the foreground (or in a separate console)
  - `java -jar -Djava.security.policy=security.policy ./honours-client/target/honours-client-2.0.0-SNAPSHOT-jar-with-dependencies.jar`

## Operation examples

Click the images linking to console recordings.

### New student and course

New student and course details persisted and assessed.

[![asciicast](https://asciinema.org/a/242354.svg)](https://asciinema.org/a/242354)

### Existing student and course

Existing student and course loaded and assessed, then deleted.

[![asciicast](https://asciinema.org/a/242357.svg)](https://asciinema.org/a/242357)

### Input validation examples

[![asciicast](https://asciinema.org/a/242358.svg)](https://asciinema.org/a/242358)

## Conclusion

## References