[![Build Status](https://travis-ci.org/Lisikhs/geronimo.svg?branch=develop)](https://travis-ci.org/Lisikhs/geronimo)

# Geronimo messaging platform

## How to migrate the database
To migrate the database please use the following command: \
`mvnw flyway:migrate`

This command will run scripts contained in `src/resources/db/migration` one by one
and make will make your database up-to-date.

## How to run the application
Just execute a command: \
`mvnw clean spring-boot:run`

Please make sure you migrate your database before running the app, otherwise it might fail to start.
To migrate the database and start the app, please use:

`mvnw clean flyway:migrate spring-boot:run`

## How to run tests
To run tests please use the following command: \
`mvnw clean test -Ptest`

This command will implicitly run a command flyway:migrate with profile 'test', 
so that the test database will be migrated before tests are run
