# GA4GH-DOS-Server

A work in progress. API modeled according to the swagger specification provided by GA4GH located [here](https://ga4gh.github.io/data-object-service-schemas/#/).

## To Run

`mvn clean spring-boot:run`


## Dependency Checklist

* Using Springboot 1.5.15 because 2.x does not have a keycloak adaptor.
* Java 1.8. Will not work with Java 10.
* MYSQL 5.7. Will not work with mysql 8.
* Running mysql on localhost:3306.
* Running Keycloak on localhost:8081.
* localhost:8080 is reserved for the server.

### MYSQL Details

* Make a user named "dos" with a password "dos"
* grant all privileges
* Make a database called "db_example"

Go into root user account in MYSQL `mysql -u root -p`, enter root user password, and execute this:

```
CREATE USER 'dos'@'localhost' IDENTIFIED BY 'dos';
GRANT ALL PRIVILEGES ON * . * TO 'dos'@'localhost';
CREATE DATABASE db_example;
quit;
```

If before quiting you want to check that the user and database was created

```
SELECT User FROM mysql.user;
SHOW DATABASES;
```

### Keycloak Details

* My **Realm** is called _"dos-server"_
* My **Client** is called _"dos-server-app"_
* I have a **Role** called _"user"_
* Under **Users** I have a user called _"testuser"_ assigned to the above **Role**

I am using Keycloak 4.0.0. By defaul the standalone server runs on port 8080. I changed it to port 8081 by doing the following.

When unzipped, the Keycloak download should produce a directory called "keycloak-4.0.0.Final". Go to 
`keycloak-4.0.0.Final/standalone/configuration`. Edit the file `standalone.xml`.

line 565 (towards the end of the file) is the following:
`<socket-binding name="http" port="${jboss.http.port:8080}"/>`

Change it to
`<socket-binding name="http" port="${jboss.http.port:8081}"/>`