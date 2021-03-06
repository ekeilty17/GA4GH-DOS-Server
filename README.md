# GA4GH-DOS-Server

Global Alliance for Genomics and Health (GA4GH) is an international, nonprofit alliance formed to accelerate the potential of research and medicine to advance human health. They have developed the Data Object Service (DOS), which is an emerging standard for specifying location of data across different cloud environments. This is an implementation of a DOS Server, which hosts and allows the discovery of data objects. The GA4GH specification of the DOS Server API is found [here](https://ga4gh.github.io/data-object-service-schemas/#/).

## Table of Contents
* [Dependency Checklist](#dependency-checklist)
* [Set Up](#set-up)
  * [MYSQL Set Up](#mysql-set-up)
  * [KeyCloak Set Up](#keycloak-set-up)
  * [Turn off KeyCloack Authentication](#turn-off-keycloak-authentication)
  * [Unit Tests](#unit-tests)
* [Usage](#usage)
  * [Command Line](#command-line)
  * [Environment Variables](#environment-variables)
* [Applications of the DOS Server](applications-of-the-dos-server)

## Dependency Checklist

* Springboot 1.5.15.
* Java 1.8. (Will not work with Java 10)
* MYSQL 5.7. (Will not work with MYSQL 8)
* Keycloak 4.0.0.
* DOS Server running on http://localhost:8080/.
* MYSQL running on http://localhost:3306/.
* Keycloak standalone server running on http://localhost:8180/.

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

Download KeyCloak 4.0.0 [here](https://www.keycloak.org/archive/downloads-4.0.0.html).

Run standalone server on **port 8180**
```
$ ./standalone.sh -Djboss.socket.binding.port-offset=100
```

**KeyCloak Config:**
* Create a **Realm** called "DNAstack"
* Create a **Client** called "dos-server-app"
* Under **Client** change "Valid Redirect URIs" to "*"
* Create a **Role** called "user"
* Create a **Users** called "testuser" with password "testuser" and assign to the **Role** "user"
* Create a **Role** called "admin"
* Create a **Users** called "adminuser" with password "adminuser" and assign to the **Role** "admin"

### Turn off KeyCloak Authentication

This is not recommended if the DOS Server is being used for commercial purposes, but for demonstration purposes or further development it might be more convient to simply turn off the authentication. This is how one would do so.

Go into the file `src/main/java/com/dnastack/dos/server/config/SecurityConfig.java`.

Change
```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);

            http.csrf().disable()
                .authorizeRequests()
                //.antMatchers("/noneSecurity").hasRole("admin")
                .antMatchers("/databundles/**").hasAnyRole("admin","user")
                .antMatchers("/dataobjects/**").hasRole("admin")
                .anyRequest()
                .permitAll();

    } 
```
to
```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);

            http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/noneSecurity").hasRole("admin")
                //.antMatchers("/databundles/**").hasAnyRole("admin","user")
                //.antMatchers("/dataobjects/**").hasRole("admin")
                .anyRequest()
                .permitAll();

    }
```


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

## Usage

### Command Line
Once the above has been completed, simply execute:

```

$ mvn clean install
$ mvn clean spring-boot:run

```

The DOS Server should be running on http://localhost:8080/

For details on the api topology and how to use to DOS Server, refer to the [GA4GH swaggerhub specification](https://ga4gh.github.io/data-object-service-schemas/#/).

### Environment Variables

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

This allows for custom configurations of the dos server.

## Applications of the DOS Server

I have created 2 applications that use this implementaiton of a Dos Server.

1. [PGP Wrapper](https://github.com/ekeilty17/DOS-Server-PGP-Wrapper) loads data from [PGP Canada](https://personalgenomes.ca/data) into a DOS Server database.
2. [GCP Wrapper](https://github.com/ekeilty17/DOS-Server-GCP-Wrapper) loads data from a public [GCP Bucket](https://cloud.google.com/storage/docs/json_api/v1/buckets) into a DOS Server database.
