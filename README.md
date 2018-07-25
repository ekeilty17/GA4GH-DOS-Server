# GA4GH-DOS-Server

Global Alliance for Genomics and Health (GA4GH) is an international, nonprofit alliance formed to accelerate the potential of research and medicine to advance human health. They have developed the Data Object Service (DOS), which is an emerging standard for specifying location of data across different cloud environments. This is an implementation of a DOS Server, which hosts and allows the discovery of data objects. The GA4GH specification of the DOS Server API is found [here](https://ga4gh.github.io/data-object-service-schemas/#/).

## Table of Contents
* [Dependency Checklist](#dependency-checklist)
* [Set Up](#set-up)
  * [MYSQL Set Up](#mysql-set-up)
  * [KeyCloak Set Up](#keycloak-set-up)
  * [Unit Tests](#unit-tests)
* [To Use](#to-use)

## Dependency Checklist

* Using Springboot 1.5.15.
* Java 1.8. Will not work with Java 10.
* MYSQL 5.7. Will not work with MYSQL 8.
* Keycloak 4.0.0.
* DOS Server running on localhost:8080.
* MYSQL running on localhost:3306.
* Keycloak running on localhost:8081.

## Set Up

### MYSQL Set Up

* Make a user named "travis" with a password ""
* grant all privileges
* Make a database called "dos"

**Step by Step**

Go into root user account in MYSQL `mysql -u root -p`, enter root user password, and execute:

```
CREATE USER 'travis'@'localhost' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON * . * TO 'travis'@'localhost';
CREATE DATABASE dos;
```

If before quiting you want to check that the user and database was created

```
SELECT User FROM mysql.user;
```
Should display list of users, one of which should be named "dos"

```
SHOW DATABASES;
```
Should display list of databases, one of which should be named "dos"

```
USE dos;
SHOW tables;
```
Should display `Empty set (0.00 sec)`

### KeyCloak Set Up

KeyCloak has not yet been fully integrated.

### Unit Tests

```

mvn test

```

At the end should display

```
Results :

Tests run: 31, Failures: 0, Errors: 0, Skipped: 0
```

## To Use

Once the above has been completed, simply execute:

```

mvn clean spring-boot:run

```

For details on the api topology and how to use to DOS Server, refer to the [GA4GH swaggerhub specification](https://ga4gh.github.io/data-object-service-schemas/#/).
