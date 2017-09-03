# Subject Hub

## Master build status
[![Build Status](https://travis-ci.com/yaseen95/subjecthub.svg?token=ZBxgtsxTU6WzeE88dbTf&branch=master)](https://travis-ci.com/yaseen95/subjecthub)

## Setup
### Angular
1. Follow angular install instructions.
2. `cd frontend`
3. `npm install` if you haven't installed yet. It may warn about fsevents. This is
safe to ignore.
4. `npm start` or `ng serve --open`
5. Press CTRL+C or CTRL+Q to exit

### Java / Spring
NOTE: Use `run-tests.sh` to run tests for both the backend and frontend.
#### Maven
##### Running App
1. `cd backend`
2. `mvn spring-boot:run`
3. Press CTRL+C or CTRL+Q to exit

##### Running tests
1. `mvn clean test -Ptest`

#### Intellij

##### Intellij Directory Setup
Before we start running we need to make sure that Intellij understands our package structure. It 
should figure it out by automatically, but just in case follow these instructions.

For each of the following directories make sure that they've been correctly marked. You can mark a
directory by right clicking on it and selecting `Mark Directory as` and choosing an option.

|Directory|Mark as|
|---------|-------|
|`backend/src/main/java`|Sources Root|
|`backend/src/main/resources`|Resources Root|
|`backend/src/test/java`|Test Sources Root|
|`backend/src/test/resources`|Test Resources Root|

If a directory has already been marked you can ignore it.

##### Running App
Navigate to `backend/src/main/java/com/example/subject/hub/` and right click on Application and 
select `Run Application`.

##### Running Tests
Because we have a different spring profile for tests we need to pass an argument so that spring 
knows which profile to use.

1. Navigate to `backend/src/test/`
2. Right click on `java` and select `Run All Tests`
3. If it doesn't fail you're all set up and everything is working fine. You can skip this section.
4. Otherwise, in intellij, on the top next to the "play" button there should be a dropdown saying
 "All in backend". Click on the dropdown and select "Edit Configurations"
5. In the dialog, next to "VM Options" add `-Dspring.profiles.active=test` after `-ea`. DON'T 
REMOVE `-ea`!
6. Press "OK"
7. Click on the play icon and run the tests. Everything should be working fine now.

## Backend
### Database
To connect to the embedded h2 database go to http://localhost:8080/h2-console/. In the JDBC url 
enter: jdbc:h2:mem:testdb, and then connect. No password is necessary.

The database is initialised with entries inside `backend/src/main/resources/data.sql`. If you wish
to add data, add it there.

#### Flyway
Flyway is used to manage database migrations. It is located in 
`backend/src/main/resources/db/migration/{platform}` where platform is the database driver.

The scripts in that file should only be used for schema migrations. If you wish to initialise the
database with entries, it should be done inside data.sql and not in the flyway directory.
