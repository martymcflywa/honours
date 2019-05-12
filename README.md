# CSI3344: Honours RMI programming project <!-- omit in toc -->

![Travis (.org)](https://img.shields.io/travis/martymcflywa/honours.svg?) ![Coveralls github](https://img.shields.io/coveralls/github/martymcflywa/honours.svg) ![Codacy grade](https://img.shields.io/codacy/grade/220174f7b8e746189207af9988f7ce0b.svg)

## Martin Ponce 10371381 <!-- omit in toc -->
### Due Monday 20th May 2019 <!-- omit in toc -->

This document and project source code is also available at  
https://github.com/martymcflywa/honours

# Table of contents <!-- omit in toc -->

- [Executive summary](#executive-summary)
- [What is remote method invocation (RMI)](#what-is-remote-method-invocation-rmi)
- [Application overview](#application-overview)
- [Requirements](#requirements)
- [DevOps](#devops)
- [Design and implementation](#design-and-implementation)
  - [Service description](#service-description)
    - [`rmiregistry`](#rmiregistry)
    - [`honours-persist`](#honours-persist)
    - [`honours-server`](#honours-server)
    - [`honours-client`](#honours-client)
  - [Package description](#package-description)
- [Setup and usage](#setup-and-usage)
  - [Script invocation](#script-invocation)
  - [Authentication](#authentication)
- [Operation examples](#operation-examples)
  - [New student and course](#new-student-and-course)
  - [Existing student and course](#existing-student-and-course)
  - [Input validation examples](#input-validation-examples)
- [Conclusion](#conclusion)
- [References](#references)

# Executive summary

This project demonstrates remote method invocation through implementation of a three tiered application. The application functions as an assessment tool for students who wish to determine whether or not they are eligible for honours study, by entering their unit marks. The application then displays the assessment response. It also provides facilities for the student to save and recall their unit marks from a database.

The application is implemented in three tiers. The first tier hosts a persistence service, where its remote object provides functionality to save, load and delete unit marks. The second tier hosts the assessment service. It provides a remote object which applies business rules to a student's unit marks and determines whether or not the student is eligible for honours study. The final tier is the client, providing a command line user interface for a student to input their details and unit marks, and displays their eligibility response on assessment.

# What is remote method invocation (RMI)

Remote method invocation (RMI) is a method of communication between clients and servers of a distributed system. Coulouris et. al (2014) describe RMI as closely related to remote procedure calls (RPC), while Tanenbaum and Steen (2018) consider RMI as an example of RPC being fully integrated into a programming language, such as Java (p. 184).

RMI provides a way for objects to invoke methods from another "potentially remote object", while abstracting implementation details relating to remote communication (Coulouris et al., 2014, p. 204). "To the programmer, remote communication looks similar to regular Java method invocations" (Oracle, 2017).

Oracle (2017) documents the Java RMI process in the context of client-server model, where the server makes references to remote objects accessible, and clients can then call methods on those remote objects. Servers make remote objects available by registering them to the RMI registry service, and clients reference remote objects by looking them up from the RMI registry.

# Application overview

The purpose of the application is to assess a student's university course marks, to determine whether or not the student is eligible for honours study.

The application uses a distributed, three tiered approach:

1. Client: provides a commandline user interface
2. Server: performs assessment and business logic
3. Persistence: recalls/stores data to/from the server

Communication between each tier is handled via RMI.

# Requirements

- JDK 1.8+
  - Developed and tested with [OpenJDK 12](https://jdk.java.net/12/)
- [Maven 3.6+](https://archive.apache.org/dist/maven/maven-3/3.6.1/binaries/apache-maven-3.6.1-bin.zip)
  - Repository includes [maven-wrapper](https://github.com/takari/maven-wrapper) binaries
  - See `mvnw` and `mvnw.cmd`

# DevOps

- Version control: [github](https://github.com/martymcflywa/honours)
- CI/CD: [travis-ci](https://travis-ci.org/martymcflywa/honours)
- Code coverage: [coveralls](https://coveralls.io/github/martymcflywa/honours)
- Static analysis: [codacy](https://app.codacy.com/project/martymcflywa/honours/dashboard)

# Design and implementation

## Service description

### `rmiregistry`

Provided by the Java JRE, this service is responsible for binding "names to references to remote objects hosted on that computer" (Coulouris et al., 2014, p. 220). For the purpose of this assignment, only a single instance of `rmiregistry` is invoked, since all "remote" applications run on the same JVM.

Servers that implement remote objects register their implementations to their local `rmiregistry`, while clients that have dependencies of remote objects, look up remote objects on the registry.

### `honours-persist`

This service acts as a server, hosting an implementation of `IPersist`. It is responsible for persisting information about a student's course and unit marks.

Implementation of `IPersist` is deferred to `honours-persist-hibernate`, which is a dependency of this service. `honours-persist-hibernate` uses [Hibernate ORM](http://hibernate.org/orm/) to interact with a remote [MySQL](https://remotemysql.com/) server.

### `honours-server`

This service acts as both a client and a server, consuming the remote implementation of `IPersist`, while hosting an implementation of `IAuth` and `IAssess`.

This module is responsible for authentication, as well as honours study assessment. `Auth` is a basic implementation of `IAuth`, with a hardcoded username and password and no facility to register new users.

`IAssess` on the other hand, implemented by `HonoursEngine`, accepts an assessment request, and considers all the business rules specified in the assignment brief. Business rules are defined in `au.com.martinponce.honours.core.Rules`, and when applied, returns the appropriate result to the client.

`HonoursEngine` also provides methods for the client to save, load, and delete existing data, which in turn calls the remote object provided by the `honours-persist` service.

### `honours-client`

This service acts as a client, providing the front-end for this distributed system. In this case, the front-end is implemented as a commandline interface.

The user must pass authentication first, then can input their student id and course id.

The client will then try to find any persisted data via the server, which in turn invokes the load method at the persistence layer. If any data is found, the user is given the option to use the existing data to make an assessment, otherwise the user can choose to delete the existing data, and input new data.

It calls the remote implementation of `IAssess` on `honours-server` to perform the assessment, and displays the returned result to the user interface.

## Package description

- `au.com.martinponce.honours.interfaces`
  - Contains remote interfaces and common abstractions between all three tiers
- `au.com.martinponce.honours.core`
  - Contains implementations of common abstractions that are shared between all three tiers
    - For example, models that are shared between both client and server
  - It is a shared dependency

# Setup and usage

## Script invocation

Scripts are provided as convenience to automate the build and execution of the applications. A powershell script is provided for Windows, and a bash script is provided for MacOS/Linux.

The scripts are located at:

```
./scripts/start-honours.ps1
./scripts/start-honours.sh
```

The script conveniently wraps the build, and execution of the applications. Maven is invoked first, to build the project and its modules, and to package each assembly into its own self-contained executable jar.

Once the build is complete, `rmiregistry` is started as a background process, so that any servers can register their objects to the service and make them available to clients. A sleep/wait time is introduced by the script, to ensure that `rmiregistry` has fully initialized to ensure subsequent application start-ups will have an available registry service.

The persistence service is then started as a background process, as well as the server, then the client in the foreground.

If the client is shutdown gracefully, the script will also shut down persistence and server background processes.

## Authentication

- Basic implementation of authentication
- No facility to register new users
- Login details:
  - Username: admin
  - Password: admin

# Operation examples

Click the images linking to console recordings.

## New student and course

New student and course details persisted and assessed.

[![asciicast](https://asciinema.org/a/242354.svg)](https://asciinema.org/a/242354)

## Existing student and course

Existing student and course loaded and assessed, then deleted.

[![asciicast](https://asciinema.org/a/242357.svg)](https://asciinema.org/a/242357)

## Input validation examples

[![asciicast](https://asciinema.org/a/242358.svg)](https://asciinema.org/a/242358)

# Conclusion

This project explored RMI, implementing it through a three tiered Java application. However, during development, I have discovered a limitation with RMI.

The issue is that although RMI makes it trivial to implement a distributed system in Java, it appears to be in direct conflict with one of distributed system's goals that we learned about in the first lecture: Heterogeneity. RMI forces both client and server to use the same programming language. For example, I originally wanted to create a web front-end, but in order to continue using RMI, I would have had to implement the client via a Java applet, no longer supported by modern browsers, due to security reasons (Schuh, 2014).

A more modern approach could have used REST services. Further design would have been required, determining the appropriate endpoints and their request and response models, as well as deciding how to deploy the web services. However, [Spring Boot](https://spring.io/guides/gs/rest-service/) provides a framework to achieve this. RESTful services would have allowed a client written in any language to communicate to the server over HTTP, improving heterogeneity in this distributed system.

# References

- Coulouris, G., Dollimore, J., Kindberg, T., & Blair, G. (2014). Distributed systems: Concepts and design (5th ed.). Pearson Education.
- Oracle. (2017). An Overview of RMI Applications. Retrieved May 5, 2019, from https://docs.oracle.com/javase/tutorial/rmi/overview.html
- Schuh, J. (2014). Chromium Blog: The Final Countdown for NPAPI. Retrieved May 12, 2019, from https://blog.chromium.org/2014/11/the-final-countdown-for-npapi.html
- Tanenbaum, A. S., & Steen, M. Van. (2018). Distributed Systems (3rd ed.). https://doi.org/10.1145/4547.4552