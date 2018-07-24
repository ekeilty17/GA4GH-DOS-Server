# GA4GH-DOS-Server

Global Alliance for Genomics and Health (GA4GH) is an international, nonprofit alliance formed to accelerate the potential of research and medicine to advance human health. They have developed the Data Object Service (DOS), which is an emerging standard for specifying location of data across different cloud environments. This is an implementation of a DOS Server, which hosts and allows the discovery of data objects. The GA4GH specification of the DOS Server API is found [here](https://ga4gh.github.io/data-object-service-schemas/#/).


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

* Make a user named "dos" with a password "dos"
* grant all privileges
* Make a database called "dos"

**Step by Step**

Go into root user account in MYSQL `mysql -u root -p`, enter root user password, and execute:

```
CREATE USER 'dos'@'localhost' IDENTIFIED BY 'dos';
GRANT ALL PRIVILEGES ON * . * TO 'dos'@'localhost';
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

run
```

mvn clean package

```

## To Run

Once the above has been completed, simply execute:

```

mvn clean spring-boot:run

```

For details on the api topology and how to use to DOS Server, refer to the [GA4GH swaggerhub specification](https://ga4gh.github.io/data-object-service-schemas/#/).
