---
theme : "black"
transition: "convex"
highlightTheme: "darkula"
slideNumber: false
controls: false
---

# CSI3344: RMI project

![Travis (.org)](https://img.shields.io/travis/martymcflywa/honours.svg?) ![Coveralls github](https://img.shields.io/coveralls/github/martymcflywa/honours.svg) ![Codacy grade](https://img.shields.io/codacy/grade/220174f7b8e746189207af9988f7ce0b.svg)

Martin Ponce 10371381  
Monday 20th May 2019  

<p>
  <small>
    https://github.com/martymcflywa/honours<br/>
    <a href = "mailto:mponce@our.ecu.edu.au">mponce@our.ecu.edu.au</a><br/>
  </small>
</p>

---

# Overview

- Introduction
- Application overview
- Design
- Setup
- Usage
- Conclusion
- References

---

# Introduction

---

## What is RMI?

- Method of communication between clients and servers in a distributed system
- Closely related to remote procedure calls (Coulouris et. al, 2014)
- An example of RPC fully integrated into a programming language (Tanenbaum and Steen, 2018)
- To the programmer, remote communication looks similar to regular Java method invocations (Oracle, 2017)

---

## How to RMI in Java

- Oracle's [documentation](https://docs.oracle.com/javase/8/docs/technotes/guides/rmi/hello/hello-world.html) has instructions on how to implement RMI in Java (Oracle, 2019a)

---

### Remote interfaces

- Must extend `Remote` (Oracle, 2019a)
- Functions must throw `RemoteException`

---

### Remote objects

- Implement the remote interface
- Also extend `UnicastRemoteObject` (Oracle, 2019c)
  - Exports remote object
  - Obtains stub to communicate with remote object
- No longer required to generate static stubs with `rmic`
  - Deprecated since [Java 1.8](https://docs.oracle.com/javase/8/docs/api/java/rmi/server/UnicastRemoteObject.html)
- Stubs are generated dynamically by extending `UnicastRemoteObject`

---

### RMIRegistry

- Bundled with JDK
  - Can be executed as separate service (Oracle, 2019a)
- Servers register remote objects to `rmiregistry`
- Clients look up remote object from `rmiregistry`

---

### Naming

- Servers use `Naming` to register remote objects to `rmiregistry` (Oracle, 2019b)
- Clients use `Naming` to look up references to remote objects from `rmiregistry`
- Format: `//host:port/name`

---

# Application overview

---

- Purpose: Assess a student's unit marks, determine honours study eligibility
- Architecture: Distributed, three tier
  - Client: Commandline user interface
  - Server: Applies business rules to input
  - Persist: Recalls/stores data to/from database
- Communication between tiers/services
  - Handled via RMI

---

## Requirements

- JDK 1.8+
  - Developed with [OpenJDK 12](https://jdk.java.net/12/)
- Maven 3.6+
  - Repository includes [maven-wrapper](https://github.com/takari/maven-wrapper)

---

## Tools

- Version control: [github](https://github.com/martymcflywa/honours)
- CI/CD: [travis-ci](https://travis-ci.org/martymcflywa/honours)
- Code coverage: [coveralls](https://coveralls.io/github/martymcflywa/honours)
- Static analysis: [codacy](https://app.codacy.com/project/martymcflywa/honours/dashboard)

---

# Design

- Interfaces
  - `honours-interfaces`
- Three services
  - `honours-persist`
  - `honours-server`
  - `honours-client`
- Other modules
  - `honours-core`
  - `honours-persist-hibernate`

---

## `honours-interfaces`

- Contains interfaces
  - `IPersist` save student unit marks
  - `IAssess` assess marks for honours study
  - `IAuth` authenticate users
- Is available in classpath of `rmiregistry`

---

## `honours-persist`

- Acts as server
- Hosts `IPersist` remote object implementation
- Implementation deferred to `honours-persist-hibernate`
- Uses [Hibernate ORM](http://hibernate.org/orm/)
- Interacts with remote [MySQL](https://remotemysql.com/) server

---

## `honours-server`

- Acts as client and server
  - Client of `honours-persist`
    - Saves data via remote object
  - Server to `honours-client`
- Hosts `IAssess` remote object implementation
  - Applies business rules to student unit marks
  - Replies with assessment result
- Hosts `IAuth` remote object implementation
  - Performs user authentication
  - Very basic implementation

---

### `honours-client`

- Acts as a client of `honours-server`
- Functions as front-end for distributed system
- Commandline interface
- Calls `IAuth` remote object to authenticate user
- Calls `IAssess` remote object to assess unit marks
- Allows user to create/load/delete unit marks
  - Calls `honours-server` service
  - Which then calls `honours-persist` service

---

### `honours-core`

- Common abstractions shared between all three services
- ie. Models shared between both servers and clients

---

# Usage

---

## Scripts

- `./scripts/start-honours.ps1`
- `./scripts/start-honours.sh`
- Wraps build and execution
- Order of execution
  - Maven build all modules
  - Start `rmiregistry`
  - Start `honours-persist`
  - Start `honours-server`
  - Start `honours-client`

---

## Authentication

- Username: `admin`
- Password: `admin`

---

## Operation examples

- Click images for terminal recording video

---

## Qualified

[![asciicast](https://asciinema.org/a/247042.svg)](https://asciinema.org/a/247042)

---

## Needs further assessment

[![asciicast](https://asciinema.org/a/247044.svg)](https://asciinema.org/a/247044)

---

## Needs permission

[![asciicast](https://asciinema.org/a/247045.svg)](https://asciinema.org/a/247045)

---

## Not qualified

[![asciicast](https://asciinema.org/a/247046.svg)](https://asciinema.org/a/247046)

---

## Disqualified

[![asciicast](https://asciinema.org/a/247048.svg)](https://asciinema.org/a/247048)

---

# Conclusion

- Demonstrated RMI via three tiered distributed system
- Problem
  - Conflict with goal: heterogeneity
  - Forced to use Java for both server and client
- I wanted to create webapp frontend
  - Need Java applet
  - No longer supported by modern browsers (Schuh, 2014)
- Could have used REST API instead

---

## References

<small>

- Coulouris, G., Dollimore, J., Kindberg, T., & Blair, G. (2014). Distributed systems: Concepts and design (5th ed.). Pearson Education.
- Oracle. (2017). An Overview of RMI Applications. Retrieved May 5, 2019, from https://docs.oracle.com/javase/tutorial/rmi/overview.html
- Oracle. (2019a). Getting Started Using Java RMI. Retrieved May 5, 2019, from https://docs.oracle.com/javase/8/docs/technotes/guides/rmi/hello/hello-world.html#create
- Oracle. (2019b). Naming (Java Platform SE 8). Retrieved May 15, 2019, from https://docs.oracle.com/javase/8/docs/api/java/rmi/Naming.html
- Oracle. (2019c). UnicastRemoteObject (Java Platform SE 8 ). Retrieved May 5, 2019, from https://docs.oracle.com/javase/8/docs/api/java/rmi/server/UnicastRemoteObject.html
- Schuh, J. (2014). Chromium Blog: The Final Countdown for NPAPI. Retrieved May 12, 2019, from https://blog.chromium.org/2014/11/the-final-countdown-for-npapi.html
- Tanenbaum, A. S., & Steen, M. Van. (2018). Distributed Systems (3rd ed.). https://doi.org/10.1145/4547.4552

</small>