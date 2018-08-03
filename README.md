# GA4GH-DOS-Server

Global Alliance for Genomics and Health (GA4GH) is an international, nonprofit alliance formed to accelerate the potential of research and medicine to advance human health. They have developed the Data Object Service (DOS), which is an emerging standard for specifying location of data across different cloud environments. This is an implementation of a DOS Server, which hosts and allows the discovery of data objects. The GA4GH specification of the DOS Server API is found [here](https://ga4gh.github.io/data-object-service-schemas/#/).

This project was developed as part of Google Summer of Code 2018.

## Table of Contents
* [Dependency Checklist](#dependency-checklist)
* [Set Up](#set-up)
  * [MYSQL Set Up](#mysql-set-up)
  * [KeyCloak Set Up](#keycloak-set-up)
  * [Unit Tests](#unit-tests)
* [To Use](#to-use)

## Dependency Checklist

* Springboot 1.5.15.
* Java 1.8. (Will not work with Java 10)
* MYSQL 5.7. (Will not work with MYSQL 8)
* Keycloak 4.0.0.
* DOS Server running on localhost:8080.
* MYSQL running on localhost:3306.
* Keycloak standalone server running on localhost:8180.

## Set Up

### MYSQL Set Up

* Make a user named "dos" with a password "dos"
* Make a database called "dos"
* grant all privileges


**Step by Step**

Go into root user account in MYSQL `$ mysql -u root -p`, enter root user password, and execute:

```
> CREATE USER 'dos'@'localhost' IDENTIFIED BY 'dos';
> CREATE DATABASE dos;
> GRANT ALL PRIVILEGES ON dos.* TO 'dos'@'localhost';
```

If before quiting you want to check that the user and database was created

```
> SELECT User FROM mysql.user;
```
Should display list of users, one of which should be named "dos"

```
> SHOW DATABASES;
```
Should display list of databases, one of which should be named "dos"

```
> USE dos;
> SHOW tables;
Empty set (0.00 sec)
```

### KeyCloak Set Up

**Note**: KeyCloak has not yet been fully integrated.

Run standalone server on **port 8180**
```
$ ./standalone.sh -Djboss.socket.binding.port-offset=100
```

Keycloak Config:
* Create a **Realm** called "DNAstack"
* Create a **Client** called "dos-server-app"
* Under **Client** change "Valid Redirect URIs" to "*"
* Create a **Role** called "user"
* Create a **Users** called "testuser" with password "testuser" and assign to the **Role** "user"
* Create a **Role** called "admin"
* Create a **Users** called "adminuser" with password "adminuser" and assign to the **Role** "admin"


### Unit Tests

First, make sure you are running Java 1.8
```
$ javac -version
javac 1.8.x
```

To run the unit tests
```

$ mvn test

...

Results :

Tests run: 31, Failures: 0, Errors: 0, Skipped: 0
```

## To Use

Once the above has been completed, simply execute:

```

$ mvn clean install
$ mvn clean spring-boot:run

```

The DOS Server should be running on http://localhost:8080/

For details on the api topology and how to use to DOS Server, refer to the [GA4GH swaggerhub specification](https://ga4gh.github.io/data-object-service-schemas/#/).

### Evironment Variabels

Using the `-D` tag, the dos server allows you to specify a number of environment variables. The list of which is below with their defauls:

```
-Dcontext.path=/
-Dserver.port=8080
-Ddb.database=dos
-Ddb.username=dos
-Ddb.password=dos
```

Below is an example of how to run the dos server with the environment variables specified

```

$ mvn clean spring-boot:run -Dcontext.path=/user1 -Dserver.port=9090

```
